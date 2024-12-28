package com.example.gratefulnote.common.diary.data.cqrs

import com.example.gratefulnote.common.diary.data.mapper.DiaryMapper
import com.example.gratefulnote.common.diary.domain.model.FilterState
import com.example.gratefulnote.common.general.domain.model.PagingResult
import com.example.gratefulnote.database.DiaryDao
import com.example.gratefulnote.mainMenu.domain.DiaryPreview
import javax.inject.Inject

class GetPreviewDiaries @Inject constructor(
    private val dao: DiaryDao,
    private val diaryMapper: DiaryMapper,
) {
    suspend fun execute(
        filterState: FilterState,
        page: Int,
    ) : PagingResult<List<DiaryPreview>> {
        return try {
            val data = dao.getDiariesPaginated(
                filterState = filterState,
                page = page,
            ).map { diaryMapper.toDiaryPreview(it) }

            PagingResult.Succeed(
                data = data,
                prevPage = if (page == 1) null else page - 1,
                nextPage = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception){
            PagingResult.Error()
        }
    }
}