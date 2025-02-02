package com.example.gratefulnote.steps.assertion.ui

import com.example.gratefulnote.R
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import io.cucumber.java.en.Then

class DiaryItemsAssertionSteps {
    private val mainHomeRobot = MainHomeRobot()

    @Then("{int}-th diary with title {string} should have active favorite icon")
    fun assertFavoriteIcon(n : Int, title: String){
        mainHomeRobot.apply {
            waitUntilScreenAppear()
            diaryList.apply {
                val index = n - 1
                scrollToIndex(index)
                assertTitleAtIndex(index = index , title = title)
                getDiaryCardWithTitle(title = title).apply {
                    favoriteIcon.assertTag(R.drawable.ic_baseline_yellow_star)
                }
            }
        }

    }
}