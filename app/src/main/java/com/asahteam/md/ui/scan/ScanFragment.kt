package com.asahteam.md.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.asahteam.md.databinding.FragmentScanBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.ScanViewModelFactory
import com.asahteam.md.ui.utils.createCustomTempFile
import com.asahteam.md.ui.utils.reduceFileImage
import com.asahteam.md.ui.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding
    private lateinit var currentPhotoPath: String
    private val viewModel by viewModels<ScanViewModel> {
        ScanViewModelFactory.getInstance(
            requireContext()
        )
    }
    private var getFile: File? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                requireContext(),
                "Permission not granted, you need to grant the permission.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding?.let {
            it.cameraButton.setOnClickListener { startTakePhoto() }
            it.galeryButton.setOnClickListener { startGallery() }
            it.cekButton.setOnClickListener { uploadImage() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.asahteam.md",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val myFile = File(currentPhotoPath)
                myFile.let { file ->
                    getFile = file
                    binding?.scanImage?.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val selectedImg = result.data?.data as Uri
                selectedImg.let { uri ->
                    val myFile = uriToFile(uri, requireContext())
                    myFile.let { file ->
                        getFile = file
                        currentPhotoPath = file.path
                        binding?.scanImage?.setImageBitmap(BitmapFactory.decodeFile(file.path))
                    }
                }
            }
        }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )

            viewModel.scanWaste(imageMultipart).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultResponse.Error -> {
                        binding?.let {
                            it.progessBar.visibility = View.GONE
                            it.blocker.visibility = View.GONE
                        }
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultResponse.Loading -> {
                        binding?.let {
                            it.progessBar.visibility = View.VISIBLE
                            it.blocker.visibility = View.VISIBLE
                        }
                    }

                    is ResultResponse.NotFound -> {

                    }

                    is ResultResponse.Success -> {
                        binding?.let {
                            it.progessBar.visibility = View.GONE
                            it.blocker.visibility = View.GONE
                            val toResult =
                                ScanFragmentDirections.actionScanFragmentToResultFragment()
                            toResult.prediction = result.data.predictionWaste
                            toResult.predict =
                                result.data.recycleRecommendation ?: "No Recycle Recommendation"
                            toResult.action = result.data.action ?: "No Action Recommendation"
                            toResult.description = result.data.message
                            toResult.image = currentPhotoPath
                            it.cekButton.findNavController().navigate(toResult)
                        }
                    }
                }
            }
        }
    }
}