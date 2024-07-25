package com.example.gratefulnote.test_scenario.daily_reminder.test_case

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.daily_notification.data.service.BootupAlarmSetReceiver
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.utils.waitUntilSucceed
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito

class TestBootupCompletedAllEnabledDailyNotificationsAlarmSet(
    private val dailyNotificationManager: IDailyNotificationManager,
    private val dailyAlarmSetter : IDailyAlarmSetter
) {
    fun begin(){
        val broadcastReceiver = BootupAlarmSetReceiver().apply {
            dailyNotificationManager = this@TestBootupCompletedAllEnabledDailyNotificationsAlarmSet.dailyNotificationManager
        }

        broadcastReceiver.onReceive(
            context = InstrumentationRegistry.getInstrumentation().targetContext,
            intent = Intent().apply {
                action = Intent.ACTION_BOOT_COMPLETED
            }
        )

        runBlocking {
            waitUntilSucceed {
                Mockito.verify(dailyAlarmSetter, Mockito.times(1)).enableDailyAlarm(
                    hour = 3,
                    minute = 3,
                    id = 3,
                    forceToNextDay = false,
                )
                Mockito.verify(dailyAlarmSetter, Mockito.times(1)).enableDailyAlarm(
                    hour = 1,
                    minute = 1,
                    id = 1,
                    forceToNextDay = false,
                )
                Mockito.verify(dailyAlarmSetter, Mockito.never()).enableDailyAlarm(
                    hour = 2,
                    minute = 2,
                    id = 2,
                    forceToNextDay = false,
                )
            }
        }
    }
}