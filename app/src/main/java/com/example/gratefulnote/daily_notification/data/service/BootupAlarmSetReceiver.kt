package com.example.gratefulnote.daily_notification.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.gratefulnote.common.domain.receiverGoAsync
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootupAlarmSetReceiver : BroadcastReceiver() {
    @Inject
    lateinit var dailyNotificationManager : IDailyNotificationManager
    override fun onReceive(context: Context, intent: Intent) = receiverGoAsync {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED)
            return@receiverGoAsync
        dailyNotificationManager.setAlarmForAllEnabledDailyNotifications()
    }
}