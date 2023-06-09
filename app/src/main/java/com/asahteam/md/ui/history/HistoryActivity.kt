package com.asahteam.md.ui.history

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.asahteam.md.databinding.ActivityHistoryBinding
import com.asahteam.md.databinding.FragmentBlogBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.blog.BlogAdapter
import com.asahteam.md.ui.blog.BlogFragmentDirections
import com.asahteam.md.ui.utils.ScanViewModelFactory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ScanViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getHistory().observe(this) { result ->
            when (result) {
                is ResultResponse.Error -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.GONE
                        it.blocker.visibility = View.GONE
                    }
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }

                is ResultResponse.Loading -> {
                    binding?.let {
                        it.progessBar.visibility = View.VISIBLE
                        it.blocker.visibility = View.VISIBLE
                        it.notFoundTv.visibility = View.GONE
                    }
                }

                is ResultResponse.NotFound -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                        it.blocker.visibility = View.GONE
                        it.notFoundTv.visibility = View.VISIBLE
                    }
                }

                is ResultResponse.Success -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.GONE
                        it.blocker.visibility = View.GONE
                        it.historyView.adapter = HistoryAdapter(result.data.sortedByDescending { it.dateScan })
                    }
                }
            }
        }
    }


}