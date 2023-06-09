package com.asahteam.md.ui.result

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentResultBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.RewardViewModelFactory
import java.io.File

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<ResultViewModel> {
        RewardViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = ResultFragmentArgs.fromBundle(arguments as Bundle)
        binding?.let {
            it.resultOrganic.text = arguments.prediction
            it.resultRecycle.text = arguments.predict
            it.tipsTitle.text = arguments.action
            it.description.text = arguments.description
            Log.e("foto", arguments.image)
            val myFile = File(arguments.image)
            myFile.let { file ->
                it.resultImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }

            it.trashButton.setOnClickListener {
                viewModel.addPoint().observe(viewLifecycleOwner) { result ->
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
                                it.trashButton.findNavController()
                                    .navigate(R.id.action_resultFragment_to_profileFragment)
                            }
                        }
                    }
                }
            }

            it.mapButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_resultFragment_to_mapFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}