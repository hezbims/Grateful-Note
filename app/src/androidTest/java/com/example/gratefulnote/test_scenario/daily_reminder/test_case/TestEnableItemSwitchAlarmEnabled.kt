package com.example.gratefulnote.test_scenario.daily_reminder.test_case

import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.daily_reminder.test_data.prepareThreeDisabledDailyNotifications
import org.mockito.Mockito

/**
 * Memastikan alarm akan ter-set apabila switch dari item di on-kan
 */
class TestEnableItemSwitchAlarmEnabled(
    private val mainHomeRobot: MainHomeRobot,
    private val dailyReminderRobot: DailyReminderRobot,
    private val dailyAlarmSetter : IDailyAlarmSetter,
    private val db : GratefulNoteDatabase
) {
    fun begin(){
        prepare()
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot
            .toogleSwitchOnItem(hour = 2, minute = 2)
            .waitUntilSwitch(1 , isSwitchOff = false)
        Mockito.verify(dailyAlarmSetter).enableDailyAlarm(
            hour = Mockito.eq(2),
            minute = Mockito.eq(2),
            id = Mockito.anyInt(),
            forceToNextDay = Mockito.eq(false),
        )
    }

    private fun prepare(){
        db.prepareThreeDisabledDailyNotifications()
    }
}