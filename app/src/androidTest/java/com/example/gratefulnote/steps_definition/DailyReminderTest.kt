package com.example.gratefulnote.steps_definition

import android.util.Log
import com.example.gratefulnote.daily_notification._hilt_module.DailyAlarmSetterModule
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@HiltAndroidTest
@UninstallModules(DailyAlarmSetterModule::class)
class DailyReminderTest {

    @BindValue
    private val mockDailyAlarmSetter: IDailyAlarmSetter = mock(IDailyAlarmSetter::class.java)

    @Test
    fun test(){

    }
    @Before
    fun prepare(){
        `when`(mockDailyAlarmSetter.disableDailyAlarm(any(Int::class.java))).then {
            Log.e("qqq", "daily alarm setter mock")
        }
        `when`(mockDailyAlarmSetter.enableDailyAlarm(
            any(Int::class.java),
            any(Int::class.java),
            any(Int::class.java)
        )).then {
            Log.e("qqq", "daily alarm setter mock")
        }
    }
}