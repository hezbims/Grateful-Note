package com.example.gratefulnote.daily_notification.domain.service

import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.database.DailyNotification
import kotlinx.coroutines.flow.Flow

interface IDailyNotificationManager {
    suspend fun addNewDailyNotification(hour : Int, minute : Int) : Flow<ResponseWrapper<Long>>
    suspend fun toogleDailyNotification(dailyNotification: DailyNotification) : Flow<ResponseWrapper<Nothing>>
    suspend fun deleteDailyNotification(dailyNotification: DailyNotification) : Flow<ResponseWrapper<Nothing>>
}