package com.example.gratefulnote.common.diary.data.repository

import androidx.lifecycle.LiveData
import com.example.gratefulnote.common.diary.data.cqrs.DeleteDiary
import com.example.gratefulnote.common.diary.data.cqrs.GetDiaryDetails
import com.example.gratefulnote.common.diary.data.cqrs.GetDistinctYears
import com.example.gratefulnote.common.diary.data.cqrs.GetPreviewDiaries
import com.example.gratefulnote.common.diary.data.cqrs.SaveDiary
import com.example.gratefulnote.common.diary.data.cqrs.ToggleFavoriteDiary
import com.example.gratefulnote.common.diary.data.cqrs.UpdateDiaryDetails
import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.common.diary.domain.model.DiaryUserInput
import com.example.gratefulnote.common.diary.domain.model.FilterState
import com.example.gratefulnote.common.diary.domain.repository.IDiaryRepository
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.common.general.domain.model.PagingResult
import com.example.gratefulnote.mainMenu.domain.DiaryPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val getPreviewDiaries: GetPreviewDiaries,
    private val getDistinctYears: GetDistinctYears,
    private val getDiaryDetails: GetDiaryDetails,
    private val updateDiaryDetails: UpdateDiaryDetails,
    private val deleteDiary: DeleteDiary,
    private val saveDiary: SaveDiary,
    private val toggleFavoriteDiary: ToggleFavoriteDiary,

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

    override fun getDiaryDetails(id: Long): Flow<ResponseWrapper<DiaryDetails>> = flow {
        emit(ResponseWrapper.Loading())
        try {
            emit(ResponseWrapper.Succeed(
                getDiaryDetails.execute(id)
            ))
        } catch (e : Exception) {
            emit(ResponseWrapper.Error())
        }
    }

    override suspend fun toogleIsFavorite(id: Long) =
        toggleFavoriteDiary.execute(id)

    override suspend fun updateDetails(data: DiaryDetails): Flow<ResponseWrapper<Unit>> = flow {
        emit(ResponseWrapper.Loading())
        try {
            emit(ResponseWrapper.Succeed(
                updateDiaryDetails.execute(data)
            ))
        } catch (e : Exception) {
            emit(ResponseWrapper.Error())
        }
    }

    override suspend fun saveDiary(data: DiaryUserInput) =
        saveDiary.execute(data)

    override suspend fun delete(id: Long): Flow<ResponseWrapper<Unit>> =
        deleteDiary.execute(id)

    override fun getDistinctYears(): LiveData<List<Int>> = getDistinctYears.execute()
}