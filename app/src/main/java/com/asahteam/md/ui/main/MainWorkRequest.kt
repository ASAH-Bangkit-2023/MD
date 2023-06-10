package com.asahteam.md.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.asahteam.md.R
import com.asahteam.md.injection.Injection
import com.asahteam.md.ui.reminder.ReminderReceiver
import com.asahteam.md.ui.scan.ScanActivity

class MainWorkRequest(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val myContext = context

    override suspend fun doWork(): Result {
        val dataStore = Injection.getPreference(myContext)

        dataStore.getWaste().collect {
            if (it.waste == 0) {
                val intent = Intent(myContext, ScanActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(myContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                val channelId = "Channel_1"
                val channelName = "AlarmManager channel"
                val notificationManagerCompat =
                    myContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val builder = NotificationCompat.Builder(myContext, channelId)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.icon_application)
                    .setContentTitle("Ingat Point Buang Sampah")
                    .setContentText("Anda belum menggunakan kesempatan buang sampah anda untuk mendapatkan point, buang sampah anda untuk mendapatkan point hari ini !")
                    .setColor(ContextCompat.getColor(myContext, android.R.color.transparent))
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
                notificationManagerCompat.notify(ReminderReceiver.REMINDER_ID, notification)
            }
        }

        return Result.success()
    }

}