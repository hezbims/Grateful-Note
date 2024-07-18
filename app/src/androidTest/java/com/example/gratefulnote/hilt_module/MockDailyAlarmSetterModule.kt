package com.example.gratefulnote.hilt_module

import com.example.gratefulnote.daily_notification._hilt_module.DailyAlarmSetterModule
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DailyAlarmSetterModule::class]
)
object MockDailyAlarmSetterModule {
    @Provides
    @Singleton
    fun provideMockDailyReminderAlarmSetter() : IDailyAlarmSetter =
        Mockito.mock(IDailyAlarmSetter::class.java).apply {
            Mockito.`when`(
                this.disableDailyAlarm(Mockito.any(Int::class.java))
            ).then {}
            Mockito.`when`(
                this.enableDailyAlarm(
                    Mockito.any(Int::class.java),
                    Mockito.any(Int::class.java),
                    Mockito.any(Int::class.java),
                    Mockito.any(Boolean::class.java),
                )
            ).then {}
            Mockito.`when`(this.canScheduleExactAlarm()).thenReturn(true)
        }
}