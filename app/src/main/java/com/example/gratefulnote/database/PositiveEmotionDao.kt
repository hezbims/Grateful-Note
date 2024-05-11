package com.example.gratefulnote.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PositiveEmotionDao {
    @Insert
    suspend fun insert(positiveEmotion : PositiveEmotion)

    @Insert
    suspend fun insertAll(positiveEmotions : List<PositiveEmotion>)
    @Query("""
        SELECT *
        FROM positive_emotion_table
        WHERE (:month IS NULL OR month = :month) AND
              (:year IS NULL OR year = :year) AND
              (:type IS NULL OR type = :type) AND
              (:onlyFavorite = 0 OR isFavorite = 1)
        ORDER BY  year, month, day
    """)
    suspend fun getAllPositiveEmotionFromOldest(
        month : Int? = null,
        year : Int? = null,
        type : String? =  null,
        onlyFavorite : Boolean = false,
    ) : List<PositiveEmotion>

    @Query("""
        SELECT *
        FROM positive_emotion_table
        WHERE (:month IS NULL OR month = :month) AND
              (:year IS NULL OR year = :year) AND
              (:type IS NULL OR type = :type) AND
              (:onlyFavorite = 0 OR isFavorite = 1)
        ORDER BY  year DESC, month DESC, day DESC
    """)
    suspend fun getAllPositiveEmotionFromNewest(
        month : Int? = null,
        year : Int? = null,
        type : String? =  null,
        onlyFavorite : Boolean = false,
    ) : List<PositiveEmotion>
    suspend fun getAllPositiveEmotion(
        month : Int? = null,
        year : Int? = null,
        type : String? =  null,
        onlyFavorite : Boolean = false,
        isSortedLatest : Boolean = true,
    ) : List<PositiveEmotion> = if (isSortedLatest)
            getAllPositiveEmotionFromNewest(
                month = month,
                year = year,
                type = type,
                onlyFavorite = onlyFavorite
        )
        else
            getAllPositiveEmotionFromOldest(
                month = month,
                year = year,
                type = type,
                onlyFavorite = onlyFavorite
            )

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