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
    suspend fun insert(vararg dailyNotification: DailyNotification)

    @Delete
    suspend fun delete(vararg  dailyNotification: DailyNotification)

    @Update
    suspend fun update(vararg dailyNotification: DailyNotification)

    @Query("SELECT * FROM daily_notification_table")
    fun getAllDailyNotification() : Flow<Array<DailyNotification>>
}