package com.example.gratefulnote.daily_notification.domain.service

import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.database.DailyNotificationEntity
import kotlinx.coroutines.flow.Flow

interface IDailyNotificationManager {
    suspend fun addNewDailyNotification(hour : Int, minute : Int) : Flow<ResponseWrapper<Int>>
    suspend fun toogleDailyNotification(dailyNotification: DailyNotificationEntity) : Flow<ResponseWrapper<DailyNotificationEntity>>
    suspend fun deleteDailyNotification(dailyNotifications: List<DailyNotificationEntity>) : Flow<ResponseWrapper<Nothing>>
    fun getAllDailyNotification() : Flow<ResponseWrapper<List<DailyNotificationEntity>>>
    suspend fun setAlarmForAllEnabledDailyNotifications()
    fun canScheduleDailyReminder() : Boolean
}