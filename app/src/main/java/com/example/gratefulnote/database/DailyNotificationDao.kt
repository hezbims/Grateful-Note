package com.example.gratefulnote.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyNotificationDao {
    @Insert
    suspend fun insert(vararg dailyNotification: DailyNotificationEntity) : Array<Long>

    @Delete
    suspend fun delete(vararg  dailyNotification: DailyNotificationEntity)

    @Update
    suspend fun update(vararg dailyNotification: DailyNotificationEntity)

    @Query("SELECT * FROM daily_notification_table")
    fun getAllDailyNotification() : Flow<List<DailyNotificationEntity>>

    @Query("SELECT * FROM daily_notification_table WHERE id = :id")
    suspend fun getDailyNotification(id: Int) : DailyNotificationEntity?

    suspend fun updateAndGetADailyNotification(
        dailyNotification: DailyNotificationEntity
    ) : DailyNotificationEntity {
        update(dailyNotification)
        return getDailyNotification(dailyNotification.id)!!
    }
}