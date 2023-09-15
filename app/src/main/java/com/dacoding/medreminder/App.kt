package com.dacoding.medreminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(
                NotificationService.MAIN_CHANNEL_ID,
                "Main",
                NotificationManager.IMPORTANCE_HIGH
            )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}