package com.example.gratefulnote.test_scenario.daily_reminder.test_case

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.daily_notification.data.service.BootupAlarmSetReceiver
import com.example.gratefulnote.daily_notification.data.service.DailyNotificationManager
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito
import kotlin.time.Duration.Companion.milliseconds

class TestBootupCompletedAllEnabledDailyNotificationsAlarmSet(
    private val db : GratefulNoteDatabase,
    private val dailyAlarmSetter : IDailyAlarmSetter
) {
    fun begin(){
        val broadcastReceiver = BootupAlarmSetReceiver().apply {
            dailyNotificationManager = DailyNotificationManager(
                database = db,
                dailyAlarmSetter = dailyAlarmSetter
            )
        }

        broadcastReceiver.onReceive(
            context = InstrumentationRegistry.getInstrumentation().targetContext,
            intent = Intent().apply {
                action = Intent.ACTION_BOOT_COMPLETED
            }
        )

        runBlocking {
            val maxIteration = 5
            var currentIteration = 1
            while (true) {
                try {
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

                    break
                } catch (t : Throwable){
                    if (currentIteration == maxIteration)
                        throw RuntimeException("Alarm tidak ter-set")
                    currentIteration += 1
                    delay(250.milliseconds)
                }
            }
        }
    }
}