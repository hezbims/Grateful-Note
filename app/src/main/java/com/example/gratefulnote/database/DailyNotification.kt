package com.example.gratefulnote.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DailyNotification.tableName)
data class DailyNotification(
    @PrimaryKey
    val id : Long = 0,

    val minute : Int,

    val hour : Int,

    @ColumnInfo(name = "is_enabled")
    val isEnabled : Boolean,
){
    companion object {
        const val tableName = "daily_notification_table"
    }
}
