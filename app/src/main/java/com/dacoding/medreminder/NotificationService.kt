package com.dacoding.medreminder

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class NotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification() {
        val notification = NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle("Content title")
            .setContentText("Content text")
            .build()
        notificationManager.notify(1, notification)
    }

    companion object {
        const val MAIN_CHANNEL_ID = "channel_main"
    }
}