package com.example.gratefulnote.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PositiveEmotionDatabaseDao {
    @Insert
    suspend fun insert(positiveEmotion : PositiveEmotion)

    @Query("SELECT * FROM positive_emotion_table WHERE type LIKE '%' || :type || '%'")
    suspend fun getAllPositiveEmotion(type : String) : List<PositiveEmotion>

    @Query("SELECT * FROM positive_emotion_table WHERE id=:id")
    suspend fun getAPositiveEmotion(id : Long) : PositiveEmotion

    @Query("DELETE FROM positive_emotion_table WHERE id=:id")
    suspend fun delete(id : Long)


    @Query("UPDATE positive_emotion_table SET what = :what , why = :why WHERE id = :id")
    suspend fun updateData(what : String , why : String , id : Long)
}