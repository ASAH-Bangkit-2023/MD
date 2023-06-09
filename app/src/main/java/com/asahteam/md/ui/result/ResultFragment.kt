package com.asahteam.md.ui.result

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asahteam.md.databinding.FragmentResultBinding
import java.io.File

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding

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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}