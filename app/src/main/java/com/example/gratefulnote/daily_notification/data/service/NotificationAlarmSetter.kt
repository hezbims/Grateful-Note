package com.example.gratefulnote.daily_notification.data.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.gratefulnote.daily_notification.Clock

const val BROADCAST_INTENT_ID = 0

class NotificationAlarmSetter(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val broadcastPendingIntent = PendingIntent.getBroadcast(
        context,
        BROADCAST_INTENT_ID,
        Intent(context, DailyNotificationBroadcastReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun setAlarm() {
        var savedTime = Clock.getSavedClock(context).timeInMillis()
        val nowTime = Clock.getNowTimeInMillis()

        if (savedTime <= nowTime)
            savedTime += AlarmManager.INTERVAL_DAY

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            savedTime,
            AlarmManager.INTERVAL_DAY,
            broadcastPendingIntent
        )
    }

    fun cancel() =
        alarmManager.cancel(broadcastPendingIntent)
}