package com.example.gratefulnote.common.diary.data.cqrs

import androidx.room.withTransaction
import com.example.gratefulnote.common.diary.domain.model.DiaryUserInput
import com.example.gratefulnote.common.domain.ITimeProvider
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.database.Diary
import com.example.gratefulnote.database.DiaryDao
import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

/**
 * Update or Insert diary
 */
class SaveDiary @Inject constructor(
    private val dao: DiaryDao,
    private val database: GratefulNoteDatabase,
    private val timeProvider: ITimeProvider,
) {
    fun execute(input: DiaryUserInput) : Flow<ResponseWrapper<Unit>> = flow {
        emit(ResponseWrapper.Loading())
        val currentCalendar=  timeProvider.getCurrentCalendar()

        try {
            database.withTransaction {
                lateinit var diary : Diary

                if (input.id == 0L) {
                    diary = Diary(
                        what = input.title,
                        why = input.description,
                        type = input.tag,
                        isFavorite = false,
                        month = currentCalendar.get(Calendar.MONTH) + 1,
                        day = currentCalendar.get(Calendar.DAY_OF_MONTH),
                        year = currentCalendar.get(Calendar.YEAR),
                        updatedAt = currentCalendar.timeInMillis,
                        createdAt = currentCalendar.timeInMillis,
                    )
                }
                else {
                    diary = dao.getADiary(input.id).copy(
                        what = input.title,
                        why = input.tag,
                        type = input.tag,
                        updatedAt = currentCalendar.timeInMillis,
                    )
                }

                dao.saveDiary(diary)
            }

            emit(ResponseWrapper.Succeed(Unit))
        } catch (e : Exception){
            emit(ResponseWrapper.Error())
        }
    }
}