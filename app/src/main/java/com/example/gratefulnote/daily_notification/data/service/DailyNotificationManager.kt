package com.example.gratefulnote.daily_notification.data.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
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
import java.util.Calendar
import java.util.Locale

class DailyNotificationManager (private val app : Context) : IDailyNotificationManager {
    private val database = GratefulNoteDatabase.getInstance(app)
    private val dao = database.dailyNotificationDao
    private val repository: IDailyNotificationRepository = DailyNotificationRepository(dao = dao)
    private val alarmManager = app.getSystemService(AlarmManager::class.java) as AlarmManager

    override suspend fun addNewDailyNotification(hour: Int, minute: Int): Flow<ResponseWrapper<Long>> {
        return database.withTransaction {
            repository.createNewDailyNotification(hour = hour, minute = minute).map { response ->
                if (response is ResponseWrapper.ResponseSucceed){
                    val calendar = Calendar.getInstance(Locale.getDefault()).apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)

                        if (System.currentTimeMillis() + 5000 < timeInMillis)
                            add(Calendar.HOUR_OF_DAY , 24)
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        app,
                        response.data!!.toInt(),
                        Intent(app, DailyNotificationBroadcastReceiver::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    assert(Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                            alarmManager.canScheduleExactAlarms())

                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
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