package com.example.gratefulnote.daily_notification.data.repository

import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.daily_notification.domain.repository.IDailyNotificationRepository
import com.example.gratefulnote.database.DailyNotificationDao
import com.example.gratefulnote.database.DailyNotificationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DailyNotificationRepository(
    private val dao : DailyNotificationDao
) : IDailyNotificationRepository {
    override fun createNewDailyNotification(
        hour: Int,
        minute: Int
    ) = flow {
        emit(ResponseWrapper.ResponseLoading())

        try {
            val id = dao.insert(
                DailyNotificationEntity(
                    isEnabled = true,
                    hour = hour,
                    minute = minute,
                )
            ).single().toInt()
            emit(ResponseWrapper.ResponseSucceed(id))
        } catch (t : Throwable){
            emit(ResponseWrapper.ResponseError(t))
        }
    }

    override fun updateDailyNotification(
        dailyNotification: DailyNotificationEntity
    ): Flow<ResponseWrapper<Nothing>> {
        throw Exception()
    }

    override fun deleteDailyNotification(
        vararg dailyNotifications: DailyNotificationEntity
    ) = flow<ResponseWrapper<Nothing>> {
        emit(ResponseWrapper.ResponseLoading())
        dao.delete(*dailyNotifications)
        emit(ResponseWrapper.ResponseSucceed())
    }

    override fun getAllDailyNotification() = flow<ResponseWrapper<List<DailyNotificationEntity>>> {
        emit(ResponseWrapper.ResponseLoading())
        dao.getAllDailyNotification()
            .map {
                ResponseWrapper.ResponseSucceed(it)
            }
            .catch { t ->
                emit(ResponseWrapper.ResponseError(t))
            }
            .collect {
                emit(it)
            }
    }
}