package com.example.gratefulnote.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    fun insert(positiveEmotion : PositiveEmotion)

    @Query("SELECT * FROM positive_emotion_table")
    fun getAllPositiveEmotion() : LiveData<List<PositiveEmotion>>

}