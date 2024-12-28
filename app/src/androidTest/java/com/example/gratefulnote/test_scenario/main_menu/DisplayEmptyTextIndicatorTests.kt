package com.example.gratefulnote.test_scenario.main_menu

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DisplayEmptyTextIndicatorTests {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 2)
    val activityRule = createAndroidComposeRule<MainActivity>()

    val mainHomeRobot = MainHomeRobot()

    @Test
    fun shouldDisplayEmptyIndicatorText_WhenDiariesIsEmpty(){
        mainHomeRobot.emptyIndicatorText.assertVisible()
    }
}