package com.example.gratefulnote.daily_notification.domain.repository

import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.database.DailyNotification
import kotlinx.coroutines.flow.Flow

interface IDailyNotificationRepository {
    fun createNewDailyNotification(hour : Int, minute : Int) : Flow<ResponseWrapper<Long>>
    fun updateDailyNotification(dailyNotification: DailyNotification) : Flow<ResponseWrapper<Nothing>>
    fun deleteDailyNotification(dailyNotifications : Collection<DailyNotification>) : Flow<ResponseWrapper<Nothing>>
    fun getAllDailyNotification() : Flow<ResponseWrapper<List<DailyNotification>>>
}