package com.dacoding.medreminder

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {

        val title = "It's time to take your medicine"
        val message = "${intent?.getIntExtra("dosage", 0)} mg of " +
                "${intent?.getStringExtra("name")}"

        val notification = NotificationCompat.Builder(context, NotificationService.MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify((intent!!.getIntExtra("notificationID", 0)), notification)

        Log.d("TIME", "Broadcast received")
    }
}