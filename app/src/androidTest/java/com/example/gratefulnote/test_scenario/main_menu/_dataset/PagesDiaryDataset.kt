package com.example.gratefulnote.test_scenario.main_menu._dataset

import com.example.gratefulnote.test_scenario._seeder.DiarySeeder
import javax.inject.Inject

class PagesDiaryDataset @Inject constructor(
    private val diarySeeder: DiarySeeder
) {
    suspend fun execute(totalPages: Int = 2){
        diarySeeder.executeMinimal(20 * totalPages)
    }
}