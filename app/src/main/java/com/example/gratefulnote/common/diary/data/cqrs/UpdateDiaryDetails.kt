package com.example.gratefulnote.common.diary.data.cqrs

import com.example.gratefulnote.common.diary.data.mapper.DiaryMapper
import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.common.domain.ITimeProvider
import com.example.gratefulnote.database.DiaryDao
import javax.inject.Inject

class UpdateDiaryDetails @Inject constructor(
    private val dao : DiaryDao,
    private val mapper: DiaryMapper,
    private val timeProvider: ITimeProvider,
) {
    suspend fun execute(diaryDetails : DiaryDetails) {
        dao.update(mapper.toDiary(diaryDetails).copy(
            updatedAt = timeProvider.getCurrentTimeInMillis())
        )
    }
}