package com.example.gratefulnote.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "positive_emotion_table")
data class PositiveEmotion(

    val type : String,

    val what : String,

    val why : String,

    var day : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),

    var month : Int = Calendar.getInstance().get(Calendar.MONTH),

    var year : Int = Calendar.getInstance().get(Calendar.YEAR),

    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L
)