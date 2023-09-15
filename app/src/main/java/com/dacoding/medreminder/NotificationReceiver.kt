package com.dacoding.medreminder

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationReceiver(viewModel: MainViewModel) : BroadcastReceiver() {

    private val title = "It's time to take your medicine"
    private val message = "${viewModel.state.dosage} mg of ${viewModel.state.name}"


    override fun onReceive(context: Context, intent: Intent?) {

        val notification = NotificationCompat.Builder(context, NotificationService.MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(NotificationService.NOTIFICATION_ID, notification)

        Log.d("TIME", "Broadcast received")
    }
}