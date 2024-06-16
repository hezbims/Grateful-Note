package com.example.gratefulnote.test_scenario.daily_reminder.test_data

import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.test.runTest

/**
 * Menyiapkan 3 daily notification, daily notification yang kedua statusnya terdisable
 */
fun GratefulNoteDatabase.prepareThreeDisabledAndEnabledDailyNotification(){
    val dao = dailyNotificationDao
    DailyReminderTestData.threeEnabledDailyNotifications.forEachIndexed {
        index, dailyNotification ->
        runTest {
            if (index % 2 == 1)
                dao.insert(dailyNotification.copy(isEnabled = false))
            else
                dao.insert(dailyNotification)
        }
    }
}