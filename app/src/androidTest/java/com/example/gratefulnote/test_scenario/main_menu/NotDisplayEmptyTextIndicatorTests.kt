package com.example.gratefulnote.test_scenario.main_menu

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.helper.MyCustomRunnerRule
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.main_menu._dataset.OneDiaryDataset
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class NotDisplayEmptyTextIndicatorTests {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var dataset: OneDiaryDataset
    @get:Rule(order = 2)
    val seedingRule = MyCustomRunnerRule {
        hiltRule.inject()
        dataset.execute()
    }

    @get:Rule(order = 3)
    val activityRule = createAndroidComposeRule<MainActivity>()

    val mainHomeRobot = MainHomeRobot()

    @Test
    fun shouldNotDisplayEmptyIndicatorText_WhenDiariesIsNotEmpty(){
        mainHomeRobot.emptyIndicatorText.assertGone()
    }
}