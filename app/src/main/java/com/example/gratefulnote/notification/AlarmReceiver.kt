package com.example.gratefulnote.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context : Context, intent : Intent) {
        val notificationManager = ContextCompat.getSystemService(context ,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(context)

        NotificationAlarmSetter(context).setAlarm()
    }

}