package com.example.gratefulnote.common.diary.data.cqrs

import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.database.DiaryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteDiary @Inject constructor(
    private val dao: DiaryDao
) {
    fun execute(id: Long) : Flow<ResponseWrapper<Unit>> = flow {
        try {
            emit(ResponseWrapper.Loading())
            dao.delete(id)
            emit(ResponseWrapper.Succeed(Unit))
        } catch (e: Exception){
            emit(ResponseWrapper.Error())
        }
    }
}