package com.asahteam.md.ui.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.asahteam.md.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showAlarmNotification(context)
    }

    private fun showAlarmNotification(
        context: Context
    ) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_application)
            .setContentTitle("Ingat Buang Sampah")
            .setContentText("Jangan Lupa Baung Sampah Anda Hari ini!")
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        builder.setChannelId(channelId)
        notificationManagerCompat.createNotificationChannel(channel)
        val notification = builder.build()
        notificationManagerCompat.notify(REMINDER_ID, notification)
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, REMINDER_ID, intent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show()
    }

    fun setRepeatingAlarm(context: Context, time: String, interval: Long) {
        if (isTimeInvalid(time)) {
            Toast.makeText(context, "Mohon Masukkan Waktu Dengan Benar", Toast.LENGTH_SHORT).show()
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, REMINDER_ID, intent, PendingIntent.FLAG_IMMUTABLE)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * interval,
            pendingIntent
        )
        Toast.makeText(context, "Reminder Berhasil Di Set", Toast.LENGTH_SHORT).show()
    }

    private fun isTimeInvalid(time: String): Boolean {
        return try {
            val df = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
            df.isLenient = false
            df.parse(time)
            false
        } catch (e: ParseException) {
            true
        }
    }

    companion object {
        private const val TIME_FORMAT = "HH:mm"

        private const val REMINDER_ID = 100
    }
}