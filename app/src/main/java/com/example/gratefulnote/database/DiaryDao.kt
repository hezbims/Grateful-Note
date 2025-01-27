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
import com.example.gratefulnote.common.diary.domain.model.FilterState

@Dao
interface DiaryDao {
    @Insert
    suspend fun insert(diary : Diary) : Long

    @Insert
    suspend fun insertAll(diaries : List<Diary>)

    @RawQuery
    suspend fun getDiaries(query : SupportSQLiteQuery) : List<Diary>

    suspend fun getDiariesPaginated(
        filterState: FilterState,
        page: Int,
    ) : List<Diary> {
        val queryString = StringBuilder().run {
            append("SELECT * FROM positive_emotion_table ")
            filterState.run {
                val onlyFavoriteInt = if (filterState.isOnlyFavorite) 1 else 0
                append("WHERE ($onlyFavoriteInt = 0 OR is_favorite = 1) ")
                if (month != null)
                    append("AND month = $month ")
                if (year != null)
                    append("AND year = $year ")
                if (type != null)
                    append("AND type = '$type' ")
                append("ORDER BY updated_at ${if (isSortedLatest) "DESC" else "ASC"} ")
                append("LIMIT $PAGE_SIZE OFFSET ${(page - 1) * 20}")
            }
            toString()
        }
        return getDiaries(SimpleSQLiteQuery(queryString))
    }

    @Query("SELECT * FROM positive_emotion_table")
    suspend fun getAllDiaries() : List<Diary>

    @Query("SELECT DISTINCT year FROM positive_emotion_table ORDER BY year DESC")
    fun getAllYear() : LiveData<List<Int>>

    @Query("SELECT * FROM positive_emotion_table WHERE id=:id")
    suspend fun getADiary(id : Long) : Diary

    @Query("DELETE FROM positive_emotion_table WHERE id=:id")
    suspend fun delete(id : Long)

    @Query("DELETE FROM positive_emotion_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(diary: Diary)

    @Transaction
    suspend fun restoreDiaries(listDiaries : List<Diary>){
        deleteAll()
        insertAll(listDiaries)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}