package com.example.gratefulnote.test_scenario.daily_reminder.test_data

import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.test.runTest

fun GratefulNoteDatabase.prepareThreeEnabledDailyNotifications() = runTest {
    val dao = this@prepareThreeEnabledDailyNotifications.dailyNotificationDao
    for (dailyNotification in DailyReminderTestData.threeEnabledDailyNotifications)
        dao.insert(dailyNotification)
}