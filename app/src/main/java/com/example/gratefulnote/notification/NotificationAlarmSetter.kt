package com.example.gratefulnote.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

const val BROADCAST_INTENT_ID = 0

class NotificationAlarmSetter(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val broadcastPendingIntent = PendingIntent.getBroadcast(
        context,
        BROADCAST_INTENT_ID,
        Intent(context, AlarmReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun setAlarm() {
        var nowTime = Clock.getSavedClock(context).timeInMillis()

        if (nowTime < Clock.getNowTimeInMillis())
            nowTime += AlarmManager.INTERVAL_DAY

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            nowTime ,
            broadcastPendingIntent
        )
    }

    fun cancel() =
        alarmManager.cancel(broadcastPendingIntent)
}