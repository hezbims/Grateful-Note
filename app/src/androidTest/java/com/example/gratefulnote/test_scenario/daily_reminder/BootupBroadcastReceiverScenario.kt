package com.example.gratefulnote.test_scenario.daily_reminder

import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.test_scenario.daily_reminder.test_case.TestBootupCompletedAllEnabledDailyNotificationsAlarmSet
import com.example.gratefulnote.test_scenario.daily_reminder.test_data.prepareThreeDisabledAndEnabledDailyNotification
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class BootupBroadcastReceiverScenario {
    @Test
    fun testBootupCompletedAllEnabledDailyNotificatinosAlarmSet(){
        TestBootupCompletedAllEnabledDailyNotificationsAlarmSet(
            db = db,
            dailyAlarmSetter = mockDailyAlarmSetter,
        ).begin()
    }

    @Before
    fun prepare(){
        hiltRule.inject()
        db.prepareThreeDisabledAndEnabledDailyNotification()
    }

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockDailyAlarmSetter : IDailyAlarmSetter
    @Inject
    lateinit var db : GratefulNoteDatabase
}