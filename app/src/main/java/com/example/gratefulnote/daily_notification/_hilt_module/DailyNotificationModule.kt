package com.example.gratefulnote.daily_notification._hilt_module

import android.content.Context
import com.example.gratefulnote.daily_notification.data.service.DailyNotificationManager
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object DailyNotificationModule {
    @Provides
    fun provideDailyNotificationManager(
        @ApplicationContext app : Context
    ) : IDailyNotificationManager{
        return DailyNotificationManager(app)
    }
}