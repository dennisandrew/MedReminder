package com.dacoding.medreminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log

class NotificationService(
    private val context: Context,
    private val viewModel: MainViewModel
) {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification() {
        val notificationID = System.currentTimeMillis().toInt()
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("notificationID", notificationID)
        intent.putExtra("dosage", viewModel.state.dosage)
        intent.putExtra("name", viewModel.state.name)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val time = getTime(viewModel)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        Log.d("TIME", "Intent is $pendingIntent")
    }


    private fun getTime(viewModel: MainViewModel): Long {
        val minute = viewModel.state.time.minute
        val hour = viewModel.state.time.hour
        val day = viewModel.state.date.dayOfMonth
        val month = viewModel.state.date.monthValue - 1
        val year = viewModel.state.date.year
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        Log.d("TIME", "Time in millis is ${calendar.timeInMillis}")
        return calendar.timeInMillis

    }


    companion object {
        const val MAIN_CHANNEL_ID = "channel_main"
    }
}