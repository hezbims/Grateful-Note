package com.example.gratefulnote.daily_notification._hilt_module

import com.example.gratefulnote.daily_notification.data.service.DailyNotificationManager
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.database.GratefulNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DailyNotificationModule {
    @Provides
    fun provideDailyNotificationManager(
        database : GratefulNoteDatabase,
        dailyAlarmSetter: IDailyAlarmSetter
    ) : IDailyNotificationManager {
        return DailyNotificationManager(
            database = database,
            dailyAlarmSetter = dailyAlarmSetter,
        )
    }
}