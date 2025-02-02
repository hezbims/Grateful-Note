package com.example.gratefulnote.common.diary.data.cqrs

import androidx.room.withTransaction
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ToggleFavoriteDiary @Inject constructor(
    private val database: GratefulNoteDatabase
) {
    private val dao = database.diaryDao

    fun execute(id : Long) = flow {
        try {
            emit(ResponseWrapper.Loading())
            database.withTransaction {
                val diary = dao.getADiary(id)

                dao.saveDiary(diary.copy(
                    isFavorite = !diary.isFavorite
                ))
            }
            emit(ResponseWrapper.Succeed(Unit))
        } catch (e : Exception){
            emit(ResponseWrapper.Error())
        }
    }
}