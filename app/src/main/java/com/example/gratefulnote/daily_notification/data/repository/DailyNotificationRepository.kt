package com.example.gratefulnote.daily_notification.data.repository

import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.daily_notification.domain.repository.IDailyNotificationRepository
import com.example.gratefulnote.database.DailyNotification
import com.example.gratefulnote.database.DailyNotificationDao
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
                DailyNotification(
                    isEnabled = true,
                    hour = hour,
                    minute = minute,
                )
            ).single()
            emit(ResponseWrapper.ResponseSucceed(id))
        } catch (t : Throwable){
            emit(ResponseWrapper.ResponseError(t))
        }
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

    override fun getAllDailyNotification() = flow<ResponseWrapper<List<DailyNotification>>> {
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