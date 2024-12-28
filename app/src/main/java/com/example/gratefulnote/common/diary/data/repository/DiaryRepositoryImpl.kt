package com.example.gratefulnote.common.diary.data.repository

import androidx.lifecycle.LiveData
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.common.diary.data.cqrs.GetDistinctYears
import com.example.gratefulnote.common.diary.data.cqrs.GetPreviewDiaries
import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.common.diary.domain.model.FilterState
import com.example.gratefulnote.common.diary.domain.repository.IDiaryRepository
import com.example.gratefulnote.common.general.domain.model.PagingResult
import com.example.gratefulnote.mainMenu.domain.DiaryPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val getPreviewDiaries: GetPreviewDiaries,
    private val getDistinctYears: GetDistinctYears,
) : IDiaryRepository {
    override suspend fun getPreviewDiary(
        filterState: FilterState,
        page: Int
    ): PagingResult<List<DiaryPreview>> {
        return getPreviewDiaries.execute(
            filterState = filterState,
            page = page,
        )
    }

    override fun getDiaryDetails(id: Long): Flow<ResponseWrapper<DiaryDetails>> {
        throw NotImplementedError()
    }

    override suspend fun toogleIsFavorite(id: Long): Flow<ResponseWrapper<Any>> {
        throw NotImplementedError()
    }

    override suspend fun updateDetails(data: DiaryDetails): Flow<ResponseWrapper<DiaryDetails>> {
        throw NotImplementedError()
    }

    override suspend fun delete(id: Long): Flow<ResponseWrapper<Any>> {
        throw NotImplementedError()
    }

    override fun getDistinctYears(): LiveData<List<Int>> = getDistinctYears.execute()
}