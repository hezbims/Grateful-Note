package com.example.gratefulnote.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DailyNotificationEntity.tableName)
data class DailyNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    val minute : Int,

    val hour : Int,

    @ColumnInfo(name = "is_enabled")
    val isEnabled : Boolean,
){
    companion object {
        const val tableName = "daily_notification_table"
    }
}
