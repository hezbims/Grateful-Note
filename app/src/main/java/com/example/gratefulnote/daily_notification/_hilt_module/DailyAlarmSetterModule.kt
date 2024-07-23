package com.example.gratefulnote.daily_notification._hilt_module

import android.content.Context
import com.example.gratefulnote.daily_notification.data.service.DailyAlarmSetter
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DailyAlarmSetterModule {
    @Provides
    fun provideDailyReminderAlarmSetter(
        @ApplicationContext context : Context
    ) : IDailyAlarmSetter =
        DailyAlarmSetter(context)
}