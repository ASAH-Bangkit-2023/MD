package com.asahteam.md.ui.scan

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import com.asahteam.md.databinding.ActivityScanBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.blog.BlogAdapter
import com.asahteam.md.ui.blog.BlogFragmentDirections
import com.asahteam.md.ui.utils.ScanViewModelFactory
import com.asahteam.md.ui.utils.createCustomTempFile
import com.asahteam.md.ui.utils.reduceFileImage
import com.asahteam.md.ui.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var currentPhotoPath: String
    private val viewModel by viewModels<ScanViewModel> {
        ScanViewModelFactory.getInstance(
            this
        )
    }
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Permission not granted, you need to grant the permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galeryButton.setOnClickListener { startGallery() }
        binding.cekButton.setOnClickListener { uploadImage() }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(this).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
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
            if (it.resultCode == RESULT_OK) {
                val myFile = File(currentPhotoPath)
                myFile.let { file ->
                    getFile = file
                    binding.scanImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg = result.data?.data as Uri
                selectedImg.let { uri ->
                    val myFile = uriToFile(uri, this)
                    myFile.let { file ->
                        getFile = file
                        binding.scanImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
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

            viewModel.scanWaste(imageMultipart).observe(this) { result ->
                when (result) {
                    is ResultResponse.Error -> {
                        binding?.let {
                            it.progessBar.visibility = View.GONE
                        }
                        Log.e("upload",  result.error)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultResponse.Loading -> {
                        binding?.let {
                            it.progessBar.visibility = View.VISIBLE
                        }
                    }

                    is ResultResponse.NotFound -> {
                        binding?.let {
                            it.progessBar.visibility = View.GONE
                        }
                    }

                    is ResultResponse.Success -> {
                        binding?.let {
                            it.progessBar.visibility = View.GONE

//                            it.cekButton.findNavController().navigate
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}