package com.asahteam.md.ui.reminder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.asahteam.md.R
import com.asahteam.md.databinding.ActivityReminderBinding
import com.asahteam.md.ui.utils.ReminderViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReminderActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityReminderBinding
    private lateinit var reminderReceiver: ReminderReceiver

    private val viewModel by viewModels<ReminderViewModel> {
        ReminderViewModelFactory.getInstance(this@ReminderActivity)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                this,
                "Permission not granted, you need to grant the permission.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

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
            val interval: Long = when (binding.repeatGroup.checkedButtonId) {
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