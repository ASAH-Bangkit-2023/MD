package com.asahteam.md.ui.reward

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.asahteam.md.databinding.ActivityRewardBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.RewardViewModelFactory

class RewardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRewardBinding
    private val viewModel by viewModels<RewardViewModel> {
        RewardViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            reward1.setOnClickListener {
                viewModel.redeemPoint(points = 500).observe(this@RewardActivity) { result ->
                    when (result) {
                        is ResultResponse.Error -> {
                            binding.progessBar.visibility = View.GONE
                            binding.blocker.visibility = View.GONE
                            Log.e("error redeem", result.error)
                            Toast.makeText(this@RewardActivity, result.error, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is ResultResponse.Loading -> {
                            binding.progessBar.visibility = View.VISIBLE
                            binding.blocker.visibility = View.VISIBLE
                        }

                        is ResultResponse.NotFound -> {}

                        is ResultResponse.Success -> {
                            binding.progessBar.visibility = View.GONE
                            binding.blocker.visibility = View.GONE
                            finish()
                        }

                    }
                }
            }
            reward2.setOnClickListener {
                viewModel.redeemPoint(points = 1000).observe(this@RewardActivity) { result ->
                    when (result) {
                        is ResultResponse.Error -> {
                            binding.progessBar.visibility = View.GONE
                            binding.blocker.visibility = View.GONE
                            Toast.makeText(
                                this@RewardActivity,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ResultResponse.Loading -> {
                            binding.progessBar.visibility = View.VISIBLE
                            binding.blocker.visibility = View.VISIBLE
                        }

                        is ResultResponse.NotFound -> {}

                        is ResultResponse.Success -> {
                            binding.progessBar.visibility = View.GONE
                            binding.blocker.visibility = View.GONE
                            finish()
                        }
                    }
                }
            }
            reward3.setOnClickListener {
                viewModel.redeemPoint(points = 1000).observe(this@RewardActivity) { result ->
                    when (result) {
                        is ResultResponse.Error -> {
                            binding.progessBar.visibility = View.GONE
                            binding.blocker.visibility = View.GONE
                            Toast.makeText(
                                this@RewardActivity,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ResultResponse.Loading -> {
                            binding.progessBar.visibility = View.VISIBLE
                            binding.blocker.visibility = View.VISIBLE
                        }

                        is ResultResponse.NotFound -> {}

                        is ResultResponse.Success -> {
                            binding.progessBar.visibility = View.GONE
                            binding.blocker.visibility = View.GONE
                            finish()
                        }
                    }
                }
            }
        }

    }
}