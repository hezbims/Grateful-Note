package com.example.gratefulnote.common.diary.data.mapper

import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.database.Diary
import com.example.gratefulnote.mainMenu.domain.DiaryPreview
import javax.inject.Inject

class DiaryMapper @Inject constructor() {
    fun toDiaryPreview(entity: Diary) : DiaryPreview =
        DiaryPreview(
            id = entity.id,
            isFavorite = entity.isFavorite,
            createdAt = entity.createdAt,
            description = entity.why,
            title = entity.what,
            type = entity.type,
        )

    fun toDiaryDetails(diary: Diary) : DiaryDetails {
        return DiaryDetails(
            id = diary.id,
            title = diary.what,
            description = diary.why,
            type = diary.type,
            updatedAt = diary.updatedAt,
            createdAt = diary.createdAt,
            day = diary.day,
            month = diary.month,
            isFavorite = diary.isFavorite,
            year = diary.year
        )
    }

    fun toDiary(diaryDetails: DiaryDetails) : Diary {
        return Diary(
            type = diaryDetails.type,
            why = diaryDetails.description,
            what = diaryDetails.title,
            id = diaryDetails.id,
            day = diaryDetails.day,
            month = diaryDetails.month,
            year = diaryDetails.year,
            isFavorite = diaryDetails.isFavorite,
            createdAt = diaryDetails.createdAt,
            updatedAt = diaryDetails.updatedAt,
        )
    }
}