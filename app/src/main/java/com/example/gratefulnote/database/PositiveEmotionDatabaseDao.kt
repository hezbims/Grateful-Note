package com.example.gratefulnote.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PositiveEmotionDatabaseDao {
    @Insert
    suspend fun insert(positiveEmotion : PositiveEmotion)

    @Insert
    suspend fun insertAll(positiveEmotions : List<PositiveEmotion>)

    @Query("""
        SELECT *
        FROM positive_emotion_table
        WHERE (:month = 0 OR month = :month) AND
              (:year IS NULL OR year = :year) AND
              (:type = 'Semua' OR type = :type) AND
              (:onlyFavorite = 0 OR isFavorite = 1)
    """)
    suspend fun getAllPositiveEmotion(
        month : Int  = 0,
        year : Int? = null,
        type : String =  "Semua",
        onlyFavorite : Boolean = false
    ) : List<PositiveEmotion>

    @Query("SELECT DISTINCT year FROM positive_emotion_table")
    fun getAllYear() : LiveData<List<Int>>

    @Query("SELECT * FROM positive_emotion_table WHERE id=:id")
    suspend fun getAPositiveEmotion(id : Long) : PositiveEmotion

    @Query("DELETE FROM positive_emotion_table WHERE id=:id")
    suspend fun delete(id : Long)

    @Query("DELETE FROM positive_emotion_table")
    suspend fun deleteAll()

    @Update
    suspend fun normalUpdate(positiveEmotion: PositiveEmotion)

    @Transaction
    suspend fun restorePositiveEmotions(listPositiveEmotions : List<PositiveEmotion>){
        deleteAll()
        insertAll(listPositiveEmotions)
    }
}