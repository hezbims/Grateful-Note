package com.example.gratefulnote.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.gratefulnote.R

const val BROADCAST_INTENT_ID = 0

class NotificationAlarmSetter(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val broadcastPendingIntent = PendingIntent.getBroadcast(
        context,
        BROADCAST_INTENT_ID,
        Intent(context, AlarmReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.shared_preferences_name),
        Context.MODE_PRIVATE
    )

    fun setAlarm() {
        val nowTime = sharedPreferences.getLong(
            context.getString(R.string.saved_time_in_millis) , 0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            nowTime ,
            broadcastPendingIntent
        )

        with(sharedPreferences.edit()){
            putLong(
                context.getString(R.string.saved_time_in_millis),
                nowTime + 24L * 3600 * 1000
            )
            commit()
        }

    }

    fun cancel() =
        alarmManager.cancel(broadcastPendingIntent)
}