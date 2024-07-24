package com.example.gratefulnote.test_scenario.daily_reminder

import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot._common.node_interaction.TestAppDataManager
import com.example.gratefulnote.test_scenario.daily_reminder.test_case.TestAlarmPermissionGrantedAllEnabledDailyNotificationsWillBeSet
import com.example.gratefulnote.test_scenario.daily_reminder.test_case.TestBootupCompletedAllEnabledDailyNotificationsAlarmSet
import com.example.gratefulnote.test_scenario.daily_reminder.test_data.prepareThreeDisabledAndEnabledDailyNotification
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DailyReminderNoScreenScenario {
    @Test
    fun testBootupCompletedAllEnabledDailyNotificatinosAlarmSet(){
        TestBootupCompletedAllEnabledDailyNotificationsAlarmSet(
            dailyNotificationManager = dailyNotificationManager,
            dailyAlarmSetter = mockDailyAlarmSetter,
        ).begin()
    }

    @Test
    fun testAlarmPermissionGrantedAllEnabledDailyNotificationsWillBeSet(){
        TestAlarmPermissionGrantedAllEnabledDailyNotificationsWillBeSet(
            dailyAlarmSetter = mockDailyAlarmSetter,
            dailyNotificationManager = dailyNotificationManager
        ).begin()
    }

    @Before
    fun prepare(){
        hiltRule.inject()
        db.prepareThreeDisabledAndEnabledDailyNotification()
    }

    @After
    fun clear(){
        appDataManager.clearAppData()
    }

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockDailyAlarmSetter : IDailyAlarmSetter
    @Inject
    lateinit var db : GratefulNoteDatabase
    @Inject
    lateinit var dailyNotificationManager : IDailyNotificationManager

    private val appDataManager = TestAppDataManager()
}