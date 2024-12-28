package com.example.gratefulnote.test_scenario._seeder

import android.icu.util.Calendar
import com.example.gratefulnote.database.Diary
import com.example.gratefulnote.database.DiaryDao
import javax.inject.Inject

class DiarySeeder @Inject constructor(
    private val dao: DiaryDao,
) {
    suspend fun executeMinimal(totalItem: Int){
        val testCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2020)
            set(Calendar.MONTH, 1)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 1)
            set(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 1)
            set(Calendar.MILLISECOND, 0)
        }
        val diaries = List(totalItem){
            val index = it + 1
            Diary(
                type = "tipe-$index",
                what = "what-$index",
                why = "why-$index",
                updatedAt = Calendar.getInstance().apply {
                    timeInMillis = testCalendar.timeInMillis + index
                }.timeInMillis
            )
        }
        dao.insertAll(diaries)
    }
}