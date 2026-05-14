package com.example.nammahasiru.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val species = inputData.getString("species") ?: "your plant"
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel("plants", "Plant Reminders", NotificationManager.IMPORTANCE_HIGH)
            )
        }

        val notif = NotificationCompat.Builder(applicationContext, "plants")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🌱 Time to Check Your Plant!")
            .setContentText("It's been 90 days! How is your $species doing? Update its status.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        nm.notify(System.currentTimeMillis().toInt(), notif)
        return Result.success()
    }
}