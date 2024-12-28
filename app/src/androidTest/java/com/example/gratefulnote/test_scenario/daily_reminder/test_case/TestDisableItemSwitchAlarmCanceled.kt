package com.example.gratefulnote.test_scenario.daily_reminder.test_case

import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.daily_reminder.test_data.prepareThreeEnabledDailyNotifications
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class TestDisableItemSwitchAlarmCanceled(
    private val mockDailyAlarmSetter : IDailyAlarmSetter,
    private val db : GratefulNoteDatabase,
    private val mainHomeRobot: MainHomeRobot,
    private val dailyReminderRobot: DailyReminderRobot,
) {
    fun begin(){
        prepare()
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot
            .waitUntilItemCount(3)
            .toogleSwitchOnItem(minute = 2, hour = 2)
            .toogleSwitchOnItem(minute = 3, hour = 3)
            .waitUntilSwitch(2 , isSwitchOff = true)

        verify(mockDailyAlarmSetter, never()).disableDailyAlarm(1)
        verify(mockDailyAlarmSetter, times(1)).disableDailyAlarm(Mockito.eq(3))
        verify(mockDailyAlarmSetter, times(1)).disableDailyAlarm(Mockito.eq(2))
    }
    private fun prepare(){
        db.prepareThreeEnabledDailyNotifications()
    }

}