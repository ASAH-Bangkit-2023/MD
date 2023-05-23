package com.asahteam.md.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asahteam.md.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}