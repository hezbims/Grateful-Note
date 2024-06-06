package com.example.gratefulnote.test_scenario.daily_reminder

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot._common.node_interaction.TestAppDataManager
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TestAddNewDailyNotificationAlarmEnabled {
    @Test
    fun testAddNewDailyNotificationAlarmEnabled(){
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot
            .addNewAlarm(11,1)
            .waitUntilItemCount(1)
    }

    @Before
    fun before(){
        hiltRule.inject()
        appDataManager.clearAppData()
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