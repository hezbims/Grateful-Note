package com.example.gratefulnote.mainMenu.presentation.logic

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gratefulnote.common.diary.domain.model.FilterState
import com.example.gratefulnote.common.diary.domain.repository.IDiaryRepository
import com.example.gratefulnote.common.general.domain.model.PagingResult
import com.example.gratefulnote.mainMenu.domain.DiaryPreview

class DiaryPreviewPagingSource(
    private val filterState: FilterState,
    private val repository: IDiaryRepository,
) : PagingSource<Int, DiaryPreview>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DiaryPreview> {
        try {
            val currentPage = params.key ?: 1
            val pagingResult = repository.getPreviewDiary(
                filterState = filterState,
                page = currentPage,
            )
            if (pagingResult is PagingResult.Succeed) {
                val data = pagingResult.data
                return LoadResult.Page(
                    data = data,
                    prevKey = pagingResult.prevPage,
                    nextKey = pagingResult.nextPage
                )
            } else {
                return LoadResult.Error(Exception("Unknown error"))
            }
        } catch (e : Exception){
            return LoadResult.Error(Exception("Unknown error"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DiaryPreview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}