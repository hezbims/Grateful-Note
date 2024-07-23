package com.example.gratefulnote.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DailyNotificationDao {
    @Insert
    suspend fun insert(vararg dailyNotification: DailyNotificationEntity) : Array<Long>

    @Delete
    suspend fun delete(vararg  dailyNotification: DailyNotificationEntity)

    @Update
    suspend fun update(vararg dailyNotification: DailyNotificationEntity)

    @Query("SELECT * FROM ${DailyNotificationEntity.tableName}")
    suspend fun getAllDailyNotification() : List<DailyNotificationEntity>

    @Query("SELECT * FROM ${DailyNotificationEntity.tableName} WHERE is_enabled = 1")
    suspend fun getAllEnabledDailyNotifications() : List<DailyNotificationEntity>

    @Query("SELECT * FROM ${DailyNotificationEntity.tableName} WHERE id = :id")
    suspend fun getDailyNotification(id: Int) : DailyNotificationEntity?

    suspend fun updateAndGetADailyNotification(
        dailyNotification: DailyNotificationEntity
    ) : DailyNotificationEntity {
        update(dailyNotification)
        return getDailyNotification(dailyNotification.id)!!
    }
}