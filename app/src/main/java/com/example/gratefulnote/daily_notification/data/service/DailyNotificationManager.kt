package com.example.gratefulnote.daily_notification.data.service

import android.content.Context
import androidx.room.withTransaction
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.daily_notification.data.repository.DailyNotificationRepository
import com.example.gratefulnote.daily_notification.domain.repository.IDailyNotificationRepository
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.database.DailyNotificationEntity
import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DailyNotificationManager (app : Context) : IDailyNotificationManager {
    private val database = GratefulNoteDatabase.getInstance(app)
    private val repository: IDailyNotificationRepository = DailyNotificationRepository(
        dao = database.dailyNotificationDao,
    )
    private val dailyAlarmSetter = DailyAlarmSetter(app)

    override suspend fun addNewDailyNotification(hour: Int, minute: Int): Flow<ResponseWrapper<Int>> {
        return database.withTransaction {
            repository.createNewDailyNotification(hour = hour, minute = minute).map { response ->
                if (response is ResponseWrapper.ResponseSucceed)
                    dailyAlarmSetter.enableDailyAlarm(
                        hour = hour,
                        minute = minute,
                        id = response.data!!,
                    )
                response
            }
        }
    }

    override suspend fun toogleDailyNotification(dailyNotification: DailyNotificationEntity): Flow<ResponseWrapper<Nothing>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDailyNotification(
        dailyNotifications: List<DailyNotificationEntity>
    )
    = flow<ResponseWrapper<Nothing>> {
        emit(ResponseWrapper.ResponseLoading())
        repository.deleteDailyNotification(
            *dailyNotifications.toTypedArray()
        ).collect {
            // TODO : cancel semua alarm yang nyala

            emit(ResponseWrapper.ResponseSucceed())
        }
    }

    override fun getAllDailyNotification(): Flow<ResponseWrapper<List<DailyNotificationEntity>>> =
        repository.getAllDailyNotification()
}