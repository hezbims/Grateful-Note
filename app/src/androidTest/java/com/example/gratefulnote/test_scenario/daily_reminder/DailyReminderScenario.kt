package com.example.gratefulnote.test_scenario.daily_reminder

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot._common.node_interaction.TestAppDataManager
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.daily_reminder.test_case.TestAddNewDailyNotificationAlarmEnabled
import com.example.gratefulnote.test_scenario.daily_reminder.test_case.TestDeleteEnabledItemAlarmCanceled
import com.example.gratefulnote.test_scenario.daily_reminder.test_case.TestDisableItemSwitchAlarmCanceled
import com.example.gratefulnote.test_scenario.daily_reminder.test_case.TestEnableItemSwitchAlarmEnabled
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DailyReminderScenario {

    /**
     * memastikan kalau item dihapus,
     * maka alarm nya akan tercancel
     */
    @Test
    fun testDisableItemSwitchAlarmCanceled(){
        TestDisableItemSwitchAlarmCanceled(
            mockDailyAlarmSetter = mockDailyAlarmSetter,
            db = db,
            mainHomeRobot = mainHomeRobot,
            dailyReminderRobot = dailyReminderRobot
        ).begin()
    }

    @Test
    fun testDeleteItemAlarmCanceled(){
        TestDeleteEnabledItemAlarmCanceled(
            dailyReminderRobot = dailyReminderRobot,
            db = db,
            mainHomeRobot = mainHomeRobot,
            mockDailyAlarmSetter = mockDailyAlarmSetter,
        ).begin()
    }

    @Test
    fun testAddNewDailyNotificationAlarmEnabled(){
        TestAddNewDailyNotificationAlarmEnabled(
            mainHomeRobot = mainHomeRobot,
            dailyReminderRobot = dailyReminderRobot,
            mockDailyAlarmSetter = mockDailyAlarmSetter,
        ).begin()
    }

    @Test
    fun testEnabletemSwitchAlarmEnabled(){
        TestEnableItemSwitchAlarmEnabled(
            dailyReminderRobot = dailyReminderRobot,
            mainHomeRobot = mainHomeRobot,
            dailyAlarmSetter = mockDailyAlarmSetter,
            db = db,
        ).begin()
    }

    @Before
    fun prepare(){
        hiltRule.inject()
        appDataManager.clearAppData()
    }

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockDailyAlarmSetter: IDailyAlarmSetter
    @Inject
    lateinit var db : GratefulNoteDatabase

    private val appDataManager = TestAppDataManager()
    private val mainHomeRobot = MainHomeRobot()
    private val dailyReminderRobot = DailyReminderRobot(composeRule)
}