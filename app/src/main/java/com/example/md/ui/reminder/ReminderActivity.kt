package com.example.md.ui.reminder

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.md.R
import com.example.md.databinding.ActivityReminderBinding
import com.example.md.ui.utils.AlarmViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReminderActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityReminderBinding
    private lateinit var reminderReceiver: ReminderReceiver

    private val viewModel by viewModels<ReminderViewModel> {
        AlarmViewModelFactory.getInstance(this@ReminderActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reminderReceiver = ReminderReceiver()

        viewModel.getAlarm().observe(this@ReminderActivity) { alarm ->
            Log.e("preference", alarm.toString())
            if (alarm.hour.isNotEmpty() && alarm.repeat != 0L) {
                binding.jam.text = alarm.hour
                val id = when (alarm.repeat) {
                    1L -> R.id.hari_1
                    3L -> R.id.hari_3
                    5L -> R.id.hari_5
                    else -> R.id.hari_7
                }
                binding.repeatGroup.check(id)
            } else {
                binding.jam.text = getString(R.string.jam)
                binding.repeatGroup.clearChecked()
            }
        }

        binding.clockButton.setOnClickListener {
            val timePickerFragmentOne = TimePickerFragment()
            timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_TAG)
        }

        binding.reminderButton.setOnClickListener {
            val reminderTime = binding.jam.text.toString()
            val id = binding.repeatGroup.checkedButtonId
            val interval: Long = when (id) {
                R.id.hari_1 -> 1L
                R.id.hari_3 -> 3L
                R.id.hari_5 -> 5L
                else -> 7L
            }
            reminderReceiver.setRepeatingAlarm(
                this,
                reminderTime,
                interval
            )
            viewModel.saveAlarm(reminderTime, interval)
        }

        binding.cancelAlarm.setOnClickListener {
            reminderReceiver.cancelAlarm(this)
            viewModel.cancelAlarm()
        }

        binding.allowNotificationCard.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
            startActivity(intent)
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (tag == TIME_PICKER_TAG) {
            binding.jam.text = dateFormat.format(calendar.time)
        }
    }

    companion object {
        private const val TIME_PICKER_TAG = "TimePicker"
    }
}