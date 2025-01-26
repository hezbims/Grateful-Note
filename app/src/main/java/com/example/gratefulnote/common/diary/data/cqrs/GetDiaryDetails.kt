package com.example.gratefulnote.common.diary.data.cqrs

import com.example.gratefulnote.common.diary.data.mapper.DiaryMapper
import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.database.DiaryDao
import javax.inject.Inject

class GetDiaryDetails @Inject constructor(
    private val diaryDao: DiaryDao,
    private val diaryMapper: DiaryMapper
) {
    suspend fun execute(id: Long) : DiaryDetails {
        return diaryMapper.toDiaryDetails(diaryDao.getADiary(id = id))
    }
}