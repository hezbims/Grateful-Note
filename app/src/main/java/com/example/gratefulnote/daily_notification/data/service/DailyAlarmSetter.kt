package com.example.gratefulnote.daily_notification.data.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.gratefulnote.daily_notification.data.constant.DailyReminderKey
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import java.util.Calendar
import java.util.Locale

/**
 * Berfungsi untuk meset dan meng-unset alarm dengan alarm manager
 */
class DailyAlarmSetter(private val context: Context) : IDailyAlarmSetter {
    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    override fun enableDailyAlarm(
        hour: Int,
        minute: Int,
        id: Int,
        forceToNextDay : Boolean
    ){
        val alarmTime = Calendar.getInstance(Locale.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (forceToNextDay || System.currentTimeMillis() + 3000 > timeInMillis)
                add(Calendar.HOUR_OF_DAY , 24)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            Intent(context, DailyNotificationBroadcastReceiver::class.java).apply {
                putExtra(DailyReminderKey.ID, id)
                putExtra(DailyReminderKey.HOUR_OF_DAY, hour)
                putExtra(DailyReminderKey.MINUTE, minute)
                action = DailyReminderKey.ACTION
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        assert(
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                alarmManager.canScheduleExactAlarms())


        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            pendingIntent
        )
    }

    override fun disableDailyAlarm(
        id: Int,
    ){
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            Intent(context, DailyNotificationBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    override fun canScheduleExactAlarm() =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
        alarmManager.canScheduleExactAlarms()

    fun enableDailyAlarm(intent: Intent){
        intent.let {
            val hour = it.getIntExtra(DailyReminderKey.HOUR_OF_DAY, -1)
            val minute = it.getIntExtra(DailyReminderKey.MINUTE, -1)
            val id = it.getIntExtra(DailyReminderKey.ID, -1)

            assert(hour >= 0 && minute >= 0 && id >= 0)

            enableDailyAlarm(
                hour = hour,
                minute = minute,
                id = id,
                forceToNextDay = true
            )
        }

    }
}