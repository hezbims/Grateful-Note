package com.example.gratefulnote.daily_notification.data.repository

import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.daily_notification.domain.repository.IDailyNotificationRepository
import com.example.gratefulnote.database.DailyNotification
import com.example.gratefulnote.database.DailyNotificationDao
import kotlinx.coroutines.flow.Flow

class DailyNotificationRepository(
    private val dao : DailyNotificationDao
) : IDailyNotificationRepository {
    override fun createNewDailyNotification(
        hour: Int,
        minute: Int
    ): Flow<ResponseWrapper<Long>> {
        throw Exception()
    }

    override fun updateDailyNotification(
        dailyNotification: DailyNotification
    ): Flow<ResponseWrapper<Nothing>> {
        throw Exception()
    }

    override fun deleteDailyNotification(
        dailyNotifications: Collection<DailyNotification>
    ): Flow<ResponseWrapper<Nothing>> {
        throw Exception()
    }
}