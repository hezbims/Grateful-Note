package com.example.gratefulnote.test_scenario.daily_reminder.test_data

import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.test.runTest

fun GratefulNoteDatabase.prepareThreeDisabledDailyNotifications() = runTest {
    val dao = this@prepareThreeDisabledDailyNotifications.dailyNotificationDao
    for (dailyNotification in DailyReminderTestData.threeEnabledDailyNotifications)
        dao.insert(dailyNotification.copy(isEnabled = false))
}