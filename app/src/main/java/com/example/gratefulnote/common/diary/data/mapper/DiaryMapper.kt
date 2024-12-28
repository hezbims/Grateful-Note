package com.example.gratefulnote.common.diary.data.mapper

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
}