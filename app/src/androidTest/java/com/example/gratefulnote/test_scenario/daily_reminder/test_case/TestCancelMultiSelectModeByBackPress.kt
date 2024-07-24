package com.example.gratefulnote.test_scenario.daily_reminder.test_case

import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.daily_reminder.test_data.prepareThreeEnabledDailyNotifications

class TestCancelMultiSelectModeByBackPress(
    private val db : GratefulNoteDatabase,
    private val dailyReminderRobot: DailyReminderRobot,
    private val mainHomeRobot: MainHomeRobot,
) {
    fun begin(){
        prepare()
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot.apply {
            longClickOnListItem(hour = 1, minute = 1)
            toogleCheckbox(hour = 3, minute = 3)
            pressBack()
            waitUntilMultiSelectModeIsDeactivated()
        }
    }

    private fun prepare() {
        db.prepareThreeEnabledDailyNotifications()
    }
}