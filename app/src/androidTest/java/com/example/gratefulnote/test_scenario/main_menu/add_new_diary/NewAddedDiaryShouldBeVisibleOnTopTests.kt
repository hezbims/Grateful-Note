package com.example.gratefulnote.test_scenario.main_menu.add_new_diary

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.filters.MediumTest
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.helper.MyCustomRunnerRule
import com.example.gratefulnote.robot.add_new_diary.AddNewDiaryRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import com.example.gratefulnote.test_scenario.main_menu._dataset.PagesDiaryDataset
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@MediumTest
class NewAddedDiaryShouldBeVisibleOnTopTests {
    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataset : PagesDiaryDataset
    @get:Rule(order = 2)
    val datasetRunner = MyCustomRunnerRule {
        hiltRule.inject()
        dataset.execute(totalPages = 5)
    }

    @get:Rule(order = 3)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val mainHomeRobot = MainHomeRobot()
    private val addNewDiaryRobot = AddNewDiaryRobot()

    /**
     * Diberikan seorang user yang berada di tampilan main menu (home) dari aplikasi,
     *
     * ketika user scroll ke item paling bawah (item ke-40)
     *
     * dan user menambahkan sebuah diary baru,
     *
     * maka user akan kembali ke halaman awal,
     *
     * dan tampilan akan terscroll ke paling atas,
     *
     * dan diary yang barusan ditambahkan, akan terlihat pada posisi paling atas
     */
    @Test
    fun newAddedDiaryShouldAppearOnTop(){
        mainHomeRobot.apply {
            diaryList.scrollToIndex(79)
            diaryList.assertTitleAtIndex("what-1", 79)
            addNewDiaryButton.performClick()
        }

        addNewDiaryRobot.apply {
            tagDropdown.chooseTag("Serenity")
            titleTextField.type("Baru")
            descTextField.type("Deskripsi")
            saveButton.performClick()
            confirmSaveDialog.confirmButton.performClick()
        }

        mainHomeRobot.apply {
            diaryList.assertTitleAtIndex("Baru", 0)
        }
    }

}