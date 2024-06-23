package com.example.gratefulnote.daily_notification.data.constant

import com.example.gratefulnote.BuildConfig

object DailyReminderKey {
    const val HOUR_OF_DAY = "hour_of_day_extra"
    const val MINUTE = "minute_extra"
    const val ID = "id_extra"
    const val ACTION = "${BuildConfig.APPLICATION_ID}.DAILY_REMINDER_ACTION"
}