package com.example.aquaquality.utilities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.aquaquality.MainActivity
import com.example.aquaquality.R

class WarningNotification(
    private val id: Int,
    private val context: Context,
    private val title: String,
    private val body: String,
) {
    private val notificationManager =
        context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationTimestamps = mutableMapOf<Int, Long>()

    fun show() {
        val currentTime = System.currentTimeMillis()
        val lastNotificationTime = notificationTimestamps[id] ?: 0
        val timeElapsed = currentTime - lastNotificationTime

        if (timeElapsed >= 60 * 1000) { // 5 minutes in milliseconds
            Log.i("Notification", "Showing notification: $id")
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val builder = NotificationCompat.Builder(context, context.getString(R.string.notif_channel_name))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            notificationManager.notify(id, builder.build())

            // Update the last notification timestamp
            notificationTimestamps[id] = currentTime
        } else {
            Log.i("Notification", "Skipping notification: $id. Not enough time elapsed.")
        }
    }


    fun clear() {
        notificationManager.cancel(id)
    }
}
