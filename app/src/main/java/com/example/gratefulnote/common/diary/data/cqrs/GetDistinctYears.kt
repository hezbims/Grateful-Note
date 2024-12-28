package com.example.gratefulnote.common.diary.data.cqrs

import androidx.lifecycle.LiveData
import com.example.gratefulnote.database.DiaryDao
import javax.inject.Inject

class GetDistinctYears @Inject constructor(
    private val dao: DiaryDao,
) {
    fun execute() : LiveData<List<Int>> =
        dao.getAllYear()
}