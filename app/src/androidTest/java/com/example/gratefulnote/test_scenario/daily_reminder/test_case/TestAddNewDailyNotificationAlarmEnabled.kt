package com.example.gratefulnote.test_scenario.daily_reminder.test_case

import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import org.mockito.Mockito
import org.mockito.Mockito.times

class TestAddNewDailyNotificationAlarmEnabled(
    private val mainHomeRobot: MainHomeRobot,
    private val dailyReminderRobot: DailyReminderRobot,
    private val mockDailyAlarmSetter: IDailyAlarmSetter
) {
    fun begin(){
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot
            .addNewAlarm(jarumJam = 11, jarumMenit = 1)
            .waitUntilItemCount(1)
        Mockito.verify(mockDailyAlarmSetter, times(1))
            .enableDailyAlarm(
                hour = Mockito.eq(11),
                minute = Mockito.eq(5),
                id = Mockito.anyInt(),
                forceToNextDay = Mockito.eq(false),
            )
    }
}