package com.example.gratefulnote.test_scenario.daily_reminder.test_data

import com.example.gratefulnote.database.DailyNotificationEntity

object DailyReminderTestData {
    val threeEnabledDailyNotifications = List(3){
        DailyNotificationEntity(
            isEnabled = true,
            hour = it + 1,
            minute = it + 1,
            id = it + 1
        )
    }
}