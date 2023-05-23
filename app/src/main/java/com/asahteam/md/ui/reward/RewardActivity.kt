package com.asahteam.md.ui.reward

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asahteam.md.databinding.ActivityRewardBinding

class RewardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}