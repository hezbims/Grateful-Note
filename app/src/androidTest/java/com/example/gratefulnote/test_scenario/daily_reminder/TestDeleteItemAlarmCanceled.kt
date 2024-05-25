package com.example.gratefulnote.test_scenario.daily_reminder

import android.util.Log
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.daily_notification._hilt_module.DailyAlarmSetterModule
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.DailyNotificationEntity
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot._common.utils.TestAppDataManager
import com.example.gratefulnote.robot.daily_reminder.DailyReminderRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DailyAlarmSetterModule::class)
class TestDeleteItemAlarmCanceled {

    @BindValue
    val mockDailyAlarmSetter: IDailyAlarmSetter = mock(IDailyAlarmSetter::class.java)

    @Inject
    lateinit var db : GratefulNoteDatabase

    @Test
    fun test(){
        mainHomeRobot.navBar.toDailyReminder()
        dailyReminderRobot
            .waitUntilItemCount(3)
            .toogleSwitchOnNthItem(minute = 2, hour = 2)
            .toogleSwitchOnNthItem(minute = 3, hour = 3)
    }
    @Before
    fun prepare(){
        hiltRule.inject()
        `when`(mockDailyAlarmSetter.disableDailyAlarm(
            any(Int::class.java))
        ).then {
            Log.e("qqq", "daily alarm setter mock")
        }
        `when`(mockDailyAlarmSetter.enableDailyAlarm(
            any(Int::class.java),
            any(Int::class.java),
            any(Int::class.java)
        )).then {
            Log.e("qqq", "daily alarm setter mock")
        }

        appDataManager.clearAppData()
        prepareDatabaseData()
    }

    private fun prepareDatabaseData(){
        val dao = db.dailyNotificationDao
        runTest {
            for (i in 1..3)
                dao.insert(
                    DailyNotificationEntity(
                        id = i,
                        minute = i,
                        hour = i,
                        isEnabled = true,
                    )
                )
        }
    }

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val appDataManager = TestAppDataManager()
    private val mainHomeRobot = MainHomeRobot()
    private val dailyReminderRobot = DailyReminderRobot(composeRule)
}