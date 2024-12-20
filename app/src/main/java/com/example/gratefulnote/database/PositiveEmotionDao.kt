package com.example.gratefulnote.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface PositiveEmotionDao {
    @Insert
    suspend fun insert(positiveEmotion : PositiveEmotion) : Long

    @Insert
    suspend fun insertAll(positiveEmotions : List<PositiveEmotion>)

    @RawQuery
    suspend fun getAllPositiveEmotions(query : SupportSQLiteQuery) : List<PositiveEmotion>

    suspend fun getAllPositiveEmotion(
        month : Int? = null,
        year : Int? = null,
        type : String? =  null,
        onlyFavorite : Boolean = false,
        isSortedLatest : Boolean = true,
    ) : List<PositiveEmotion> {
        val onlyFavoriteInt = if (onlyFavorite) 1 else 0

        val queryString = StringBuilder().run {
            append("SELECT * FROM positive_emotion_table ")
            append("WHERE ($onlyFavoriteInt = 0 OR is_favorite = 1) ")
            if (month != null)
                append("AND month = $month ")
            if (year != null)
                append("AND year = $year ")
            if (type != null)
                append("AND type = '$type' ")
            append("ORDER BY updated_at ${if (isSortedLatest) "DESC" else "ASC"} ")

            toString()
        }
        return getAllPositiveEmotions(SimpleSQLiteQuery(queryString))
    }

    @Query("SELECT DISTINCT year FROM positive_emotion_table ORDER BY year DESC")
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