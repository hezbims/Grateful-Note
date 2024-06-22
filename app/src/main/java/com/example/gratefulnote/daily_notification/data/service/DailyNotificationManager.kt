package com.example.gratefulnote.daily_notification.data.service

import androidx.room.withTransaction
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.daily_notification.data.repository.DailyNotificationRepository
import com.example.gratefulnote.daily_notification.domain.repository.IDailyNotificationRepository
import com.example.gratefulnote.daily_notification.domain.service.IDailyAlarmSetter
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.database.DailyNotificationEntity
import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.flow.onEach

class DailyNotificationManager (
    private val database: GratefulNoteDatabase,
    private val dailyAlarmSetter: IDailyAlarmSetter
) : IDailyNotificationManager {
    private val repository: IDailyNotificationRepository = DailyNotificationRepository(
        dao = database.dailyNotificationDao,
    )

    override suspend fun addNewDailyNotification(hour: Int, minute: Int) =
        database.withTransaction {
            repository.createNewDailyNotification(hour = hour, minute = minute).onEach { response ->
                if (response is ResponseWrapper.Succeed)
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
                if (response is ResponseWrapper.Succeed) {
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
    ) =
        repository.deleteDailyNotification(
            *dailyNotifications.toTypedArray()
        ).onEach { response ->
            if (response is ResponseWrapper.Succeed)
                for (dailyNotification in dailyNotifications)
                    if (dailyNotification.isEnabled)
                        dailyAlarmSetter.disableDailyAlarm(dailyNotification.id)
        }

    override fun getAllDailyNotification() =
        repository.getAllDailyNotification()

    override fun canScheduleDailyReminder() = dailyAlarmSetter.canScheduleExactAlarm()
}