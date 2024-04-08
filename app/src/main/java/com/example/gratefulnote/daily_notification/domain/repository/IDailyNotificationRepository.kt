package com.example.gratefulnote.daily_notification.domain.repository

import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.database.DailyNotificationEntity
import kotlinx.coroutines.flow.Flow

interface IDailyNotificationRepository {
    fun createNewDailyNotification(hour : Int, minute : Int) : Flow<ResponseWrapper<Int>>
    fun updateDailyNotification(dailyNotification: DailyNotificationEntity) : Flow<ResponseWrapper<Nothing>>
    fun deleteDailyNotification(vararg dailyNotifications : DailyNotificationEntity) : Flow<ResponseWrapper<Nothing>>
    fun getAllDailyNotification() : Flow<ResponseWrapper<List<DailyNotificationEntity>>>
}