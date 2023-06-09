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
import java.time.LocalDate

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<ResultViewModel> {
        RewardViewModelFactory.getInstance(requireContext())
    }
    private lateinit var argument: ResultFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getWaste().observe(viewLifecycleOwner) { result ->
            if (LocalDate.now().dayOfMonth != result.dateWaste) {
                viewModel.resetWaste()
            }

            binding?.let {
                if (result.waste == 0 && argument.prediction != "No class detected") {
                    it.trashButton.visibility = View.VISIBLE
                } else {
                    it.trashButton.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Anda Sudah Membuang Sampah Hari ini, Atau Tipe Sampah Tidak Valid !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        argument = ResultFragmentArgs.fromBundle(arguments as Bundle)

        binding?.let { view1 ->
            view1.resultOrganic.text = argument.prediction
            view1.resultRecycle.text = argument.predict
            view1.tipsTitle.text = argument.action
            view1.description.text = argument.description
            Log.e("foto", argument.image)
            val myFile = File(argument.image)
            myFile.let { file ->
                view1.resultImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
            val point = when (argument.prediction) {
                "Shoes" -> 50
                "Metal" -> 25
                "Plastic" -> 100
                "Glass" -> 50
                "Clothes" -> 25
                "Paper" -> 125
                "Trash" -> 10
                "Battery" -> 30
                "Biological" -> 45
                "Cardboard" -> 70
                else -> 0
            }

            view1.trashButton.setOnClickListener {
                viewModel.addPoint(point).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ResultResponse.Error -> {
                            view1.progessBar.visibility = View.GONE
                            view1.blocker.visibility = View.GONE
                            Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                        }

                        is ResultResponse.Loading -> {
                            view1.progessBar.visibility = View.VISIBLE
                            view1.blocker.visibility = View.VISIBLE
                        }

                        is ResultResponse.NotFound -> {

                        }

                        is ResultResponse.Success -> {
                            viewModel.saveWaste(LocalDate.now().dayOfMonth)
                            view1.progessBar.visibility = View.GONE
                            view1.blocker.visibility = View.GONE
                            view1.trashButton.findNavController()
                                .navigate(R.id.action_resultFragment_to_profileFragment)
                        }
                    }
                }
            }

            view1.mapButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_resultFragment_to_mapFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}