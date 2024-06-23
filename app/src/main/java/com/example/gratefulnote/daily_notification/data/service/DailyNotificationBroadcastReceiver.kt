package com.example.gratefulnote.daily_notification.data.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.gratefulnote.daily_notification.data.constant.DailyReminderKey

class DailyNotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context : Context, intent : Intent) {
        if (intent.action != DailyReminderKey.ACTION)
            return

        val alarmSetter = DailyAlarmSetter(context)
        alarmSetter.enableDailyAlarm(intent)

        val notificationManager = ContextCompat.getSystemService(context ,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(context)
    }
}