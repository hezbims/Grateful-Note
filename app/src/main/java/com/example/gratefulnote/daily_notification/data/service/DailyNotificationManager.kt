package com.example.gratefulnote.daily_notification.data.service

import android.content.Context
import androidx.room.withTransaction
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.daily_notification.data.repository.DailyNotificationRepository
import com.example.gratefulnote.daily_notification.domain.repository.IDailyNotificationRepository
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.database.DailyNotificationEntity
import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class DailyNotificationManager (app : Context) : IDailyNotificationManager {
    private val database = GratefulNoteDatabase.getInstance(app)
    private val repository: IDailyNotificationRepository = DailyNotificationRepository(
        dao = database.dailyNotificationDao,
    )
    private val dailyAlarmSetter = DailyAlarmSetter(app)

    override suspend fun addNewDailyNotification(hour: Int, minute: Int) =
        database.withTransaction {
            repository.createNewDailyNotification(hour = hour, minute = minute).onEach { response ->
                if (response is ResponseWrapper.ResponseSucceed)
                    dailyAlarmSetter.enableDailyAlarm(
                        hour = hour,
                        minute = minute,
                        id = response.data!!,
                    )
            }
        }

    override suspend fun toogleDailyNotification(dailyNotification: DailyNotificationEntity) =
        database.withTransaction {
            repository.updateDailyNotification(
                dailyNotification.copy(isEnabled = !dailyNotification.isEnabled)
            ).onEach { response ->
                if (response is ResponseWrapper.ResponseSucceed) {
                    // dari nyala ke mati, maka cancel alarm
                    if (dailyNotification.isEnabled)
                        dailyAlarmSetter.disableDailyAlarm(dailyNotification.id)
                    else
                        dailyAlarmSetter.enableDailyAlarm(
                            hour = dailyNotification.hour,
                            minute = dailyNotification.minute,
                            id = dailyNotification.id,
                        )
                }
            }
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

    override fun getAllDailyNotification() =
        repository.getAllDailyNotification()
}