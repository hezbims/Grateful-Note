package com.example.gratefulnote.daily_notification.data.repository

import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.daily_notification.domain.repository.IDailyNotificationRepository
import com.example.gratefulnote.database.DailyNotificationDao
import com.example.gratefulnote.database.DailyNotificationEntity
import kotlinx.coroutines.flow.flow

class DailyNotificationRepository(
    private val dao : DailyNotificationDao
) : IDailyNotificationRepository {
    override fun createNewDailyNotification(
        hour: Int,
        minute: Int
    ) = flow {
        emit(ResponseWrapper.Loading())

        try {
            val id = dao.insert(
                DailyNotificationEntity(
                    isEnabled = true,
                    hour = hour,
                    minute = minute,
                )
            ).single().toInt()
            emit(ResponseWrapper.Succeed(id))
        } catch (t : Throwable){
            emit(ResponseWrapper.Error(t))
        }
    }

    override fun updateDailyNotification(
        dailyNotification: DailyNotificationEntity
    ) = flow {
        emit(ResponseWrapper.Loading())
        dao.update(dailyNotification)

        val newData = dao.getDailyNotification(dailyNotification.id)
        emit(ResponseWrapper.Succeed(data = newData))
    }

    override fun deleteDailyNotification(
        vararg dailyNotifications: DailyNotificationEntity
    ) = flow<ResponseWrapper<Nothing>> {
        emit(ResponseWrapper.Loading())
        dao.delete(*dailyNotifications)
        emit(ResponseWrapper.Succeed())
    }

    override fun getAllDailyNotification() = flow {
        emit(ResponseWrapper.Loading())
        val listDailyNotification = dao.getAllDailyNotification()
        emit(ResponseWrapper.Succeed(data = listDailyNotification))
    }
}