package com.example.md.ui.reminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.md.databinding.ActivityReminderBinding

class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}