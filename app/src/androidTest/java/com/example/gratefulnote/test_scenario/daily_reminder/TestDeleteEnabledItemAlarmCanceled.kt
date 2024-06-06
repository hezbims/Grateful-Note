package com.example.gratefulnote.test_scenario.daily_reminder

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot._common.node_interaction.TestAppDataManager
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.daily_reminder.test_data.prepareThreeEnabledDailyNotifications
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import javax.inject.Inject

/**
 * Memastikan kalau sebuah item daily notification sebelumnya dalam kondisi enable,
 * apabila item tersebut di delete,
 * maka alarm yang ada di dalam item tersebut akan tercancel
 */
@HiltAndroidTest
class TestDeleteEnabledItemAlarmCanceled {
    @Test
    fun begin(){
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot
            .waitUntilItemCount(3)
            .longClickOnListItem(hour = 1, minute = 1)
            .toogleCheckbox(hour = 3, minute = 3)
            .performDeleteSelectedItem()
            .waitUntilItemCount(1)

        verify(mockDailyAlarmSetter, only()).disableDailyAlarm(1)
        verify(mockDailyAlarmSetter, never()).disableDailyAlarm(2)
        verify(mockDailyAlarmSetter, only()).disableDailyAlarm(3)
    }

    @Before
    fun before(){
        hiltRule.inject()
        appDataManager.clearAppData()
        db.prepareThreeEnabledDailyNotifications()
    }

    @Inject
    lateinit var mockDailyAlarmSetter: IDailyAlarmSetter
    @Inject
    lateinit var db : GratefulNoteDatabase

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val appDataManager = TestAppDataManager()
    private val mainHomeRobot = MainHomeRobot()
    private val dailyReminderRobot = DailyReminderRobot(composeRule)
}