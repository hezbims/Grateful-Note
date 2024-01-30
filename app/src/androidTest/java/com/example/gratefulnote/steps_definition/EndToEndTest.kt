package com.example.gratefulnote.steps_definition

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.robot._common.utils.TestAppDataManager
import com.example.gratefulnote.robot.add_gratitude.AddGratitudeRobot
import com.example.gratefulnote.robot.backup_and_restore.BackupRestoreRobot
import com.example.gratefulnote.robot.edit_screen.EditScreenRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class EndToEndTest {
    @Test
    fun givenAnUserStartTheApp() {
        When_the_user_input_three_new_positive_emotion()
        Then_there_is_three_list_of_card_in_the_home_page()
        When_the_user_edit_the_second_positive_emotion()
        Then_the_second_positive_emotion_edited()
        When_the_user_navigate_to_backup_screen()
        And_the_user_create_a_new_backup()
        Then_a_new_backup_item_is_displayed()
    }
    @Before
    fun clearAppData() = appDataManager.clearAppData()

    private fun When_the_user_input_three_new_positive_emotion() {
        mainHomeRobot.toAddGratitude()
        addGratitudeRobot.fillFormAndSave(
            spinnerValue = "Amusement",
            whatValue = "Saya terhibur",
            whyValue = "karena diajak berwisata"
        )

        mainHomeRobot.toAddGratitude()
        addGratitudeRobot.fillFormAndSave(
            spinnerValue = "Gratitude",
            whatValue = "saya bersyukur",
            whyValue = "karena lulus ujian"
        )

        mainHomeRobot.toAddGratitude()
        addGratitudeRobot.fillFormAndSave(
            spinnerValue = "Joy",
            whatValue = "saya senang",
            whyValue = "Saya senang karena bermain sepak bola di hari ini"
        )
    }
    private fun Then_there_is_three_list_of_card_in_the_home_page() {
        mainHomeRobot
            .waitForItemCount(itemCount = 3)
            .assertNthRecyclerViewTitle(0 , "saya senang")
            .assertNthRecyclerViewTitle(1 , "saya bersyukur")
            .assertNthRecyclerViewTitle(2 , "Saya terhibur")

    }
    private fun When_the_user_edit_the_second_positive_emotion() {
        mainHomeRobot
            .toEditGratitudeWithNthItem(index = 1)
        editScreenRobot
            .replaceWhatValue("title berubah")
            .replaceWhyValue("\"\"\"\"")
            .pressBack()
    }
    private fun Then_the_second_positive_emotion_edited() {
        mainHomeRobot
            .assertNthRecyclerViewTitle(itemIndex = 1, title= "title berubah")
            .toEditGratitudeWithNthItem(index = 1)
        editScreenRobot
            .assertFieldValue(
                what = "title berubah",
                why = "\"\"\"\"",
            )
            .pressBack()
    }
    private fun When_the_user_navigate_to_backup_screen() {
        mainHomeRobot
            .navigateToBackupRestore()
    }
    private fun And_the_user_create_a_new_backup() {
        backupRestoreRobot.clickPilihLokasiBackup()
    }
    private fun Then_a_new_backup_item_is_displayed() {
    }

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val appDataManager = TestAppDataManager()
    private val mainHomeRobot = MainHomeRobot()
    private val editScreenRobot = EditScreenRobot()
    private val addGratitudeRobot = AddGratitudeRobot()
    private val backupRestoreRobot = BackupRestoreRobot(composeRule)
}