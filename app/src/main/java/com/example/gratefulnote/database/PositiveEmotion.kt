package com.example.gratefulnote.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "positive_emotion_table")
data class PositiveEmotion(

    val type : String,

    val what : String,

    val why : String,

    var day : Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),

    var month : Int = Calendar.getInstance().get(Calendar.MONTH) + 1,

    var year : Int = Calendar.getInstance().get(Calendar.YEAR),

    val isFavorite : Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L
) : Parcelable