package com.example.gratefulnote.test_scenario.main_menu._dataset

import com.example.gratefulnote.test_scenario._seeder.DiarySeeder
import javax.inject.Inject

class OneDiaryDataset @Inject constructor(
    private val diarySeeder: DiarySeeder
) {
    suspend fun execute(){
        diarySeeder.executeMinimal(1)
    }
}