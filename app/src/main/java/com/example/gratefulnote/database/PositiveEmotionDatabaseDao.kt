package com.example.gratefulnote.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PositiveEmotionDatabaseDao {
    @Insert
    suspend fun insert(positiveEmotion : PositiveEmotion)

    @Query("SELECT * FROM positive_emotion_table WHERE date LIKE '%' || :date || '%'")
    suspend fun getAllPositiveEmotion(date : String) : List<PositiveEmotion>

    @Query("DELETE FROM positive_emotion_table WHERE id=:id")
    suspend fun delete(id : Long)
}