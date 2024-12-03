package com.example.gratefulnote.test_scenario.filter_test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.robot.diary_filter_dialog.DiaryFilterRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.filter_test.test_data.FilterInstruction
import com.example.gratefulnote.test_scenario.filter_test.test_data.prepareWithFilterTestData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import javax.inject.Inject

@HiltAndroidTest
@RunWith(Parameterized::class)
class DiaryFilterTestScenario(
    private val testData: FilterInstruction
) {

    @Test
    fun shouldDisplayDataBasedOnFilterCorrectly() = runTest {
        // Prepare
        database.prepareWithFilterTestData()

        // Do
        mainHomeRobot.openFilterMenu()
        diaryFilterRobot.applyFilterInstruction(testData)

        // Assert
        for ((index , title) in testData.expectedTitleAtIndex)
            mainHomeRobot.assertNthRecyclerViewTitle(index , title)
    }


    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var database: GratefulNoteDatabase
    @Before
    fun before() = hiltRule.inject()

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val mainHomeRobot = MainHomeRobot()
    private val diaryFilterRobot = DiaryFilterRobot()

    companion object {
        @JvmStatic
        @Parameters
        fun data() : Iterable<Any>{
            return FilterInstruction.testData()
        }
    }
}