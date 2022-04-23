package com.example.gratefulnote.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

@Entity(tableName = "positive_emotion_table")
data class PositiveEmotion(
    @PrimaryKey(autoGenerate = true)
    val id : Long,

    val type : String,

    val what : String,

    val why : String,

    val date : String = SimpleDateFormat("dd/M/yyyy" , Locale.getDefault()).format(Date())
)