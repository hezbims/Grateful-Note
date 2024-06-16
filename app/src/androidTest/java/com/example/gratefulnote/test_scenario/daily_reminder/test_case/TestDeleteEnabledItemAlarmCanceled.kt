package com.example.gratefulnote.test_scenario.daily_reminder.test_case

import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.daily_reminder.test_data.prepareThreeDisabledAndEnabledDailyNotification
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

/**
 * Memastikan kalau sebuah item daily notification sebelumnya dalam kondisi enable,
 * apabila item tersebut di delete,
 * maka alarm yang ada di dalam item tersebut akan tercancel
 */
class TestDeleteEnabledItemAlarmCanceled(
    private val db : GratefulNoteDatabase,
    private val mockDailyAlarmSetter : IDailyAlarmSetter,
    private val dailyReminderRobot: DailyReminderRobot,
    private val mainHomeRobot: MainHomeRobot,
) {
    fun begin(){
        prepare()
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot
            .waitUntilItemCount(3)
            .longClickOnListItem(hour = 2, minute = 2)
            .toogleCheckbox(hour = 1, minute = 1)
            .performDeleteSelectedItem()
            .waitUntilItemCount(1)

        verify(mockDailyAlarmSetter, times(1)).disableDailyAlarm(1)
        verify(mockDailyAlarmSetter, never()).disableDailyAlarm(2)
        verify(mockDailyAlarmSetter, never()).disableDailyAlarm(3)
    }

    private fun prepare(){
        db.prepareThreeDisabledAndEnabledDailyNotification()
    }
}