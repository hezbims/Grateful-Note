package com.example.gratefulnote.common.diary.domain.repository

import androidx.lifecycle.LiveData
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.common.diary.domain.model.FilterState
import com.example.gratefulnote.common.general.domain.model.PagingResult
import com.example.gratefulnote.mainMenu.domain.DiaryPreview
import kotlinx.coroutines.flow.Flow

interface IDiaryRepository {
    suspend fun getPreviewDiary(
        filterState: FilterState,
        page: Int,
    ) : PagingResult<List<DiaryPreview>>

    fun getDiaryDetails(id: Long) : Flow<ResponseWrapper<DiaryDetails>>

    /**
     * Mengupdate ```isFavorite``` menjadi nilai yang berlawanan
     */
    suspend fun toogleIsFavorite(id: Long) : Flow<ResponseWrapper<Any>>

    suspend fun updateDetails(data: DiaryDetails) : Flow<ResponseWrapper<DiaryDetails>>

    suspend fun delete(id: Long) : Flow<ResponseWrapper<Any>>

    fun getDistinctYears() : LiveData<List<Int>>

}