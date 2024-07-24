package com.example.gratefulnote.daily_notification.data.service

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.gratefulnote.common.domain.receiverGoAsync
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmPermissionGrantedReceiver : BroadcastReceiver() {
    @Inject
    lateinit var dailyNotificationManager : IDailyNotificationManager
    override fun onReceive(context: Context, intent: Intent) = receiverGoAsync {
        if (intent.action != AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
            return@receiverGoAsync
        try {
            dailyNotificationManager.setAlarmForAllEnabledDailyNotifications()
        } catch (_: Throwable) {}
    }
}