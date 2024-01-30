package com.example.gratefulnote.steps_definition

import android.content.Context
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.common.constants.Constants
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.helper.ActivityScenarioHolder
import com.example.gratefulnote.robot._common.ComposeRuleHolder
import com.example.gratefulnote.robot.add_gratitude.AddGratitudeRobot
import com.example.gratefulnote.robot.backup_and_restore.BackupRestoreRobot
import com.example.gratefulnote.robot.edit_screen.EditScreenRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class E2ETestSteps(
    val mainHomeRobot: MainHomeRobot,
    val addGratitudeRobot: AddGratitudeRobot,
    val editScreenRobot: EditScreenRobot,
    val activityScenarioHolder: ActivityScenarioHolder,
    composeRuleHolder: ComposeRuleHolder
) {
    private val backupRestoreRobot = BackupRestoreRobot(composeRuleHolder.composeRule)

    @Before("@e2e-test")
    fun clearDatabase(){
        val appContext = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        appContext.deleteDatabase(PositiveEmotionDatabase.dbName)
        clearUrisPermissions(appContext)
        clearSharedPreference(Constants.SharedPrefs.Notification.name , appContext)
        clearSharedPreference(Constants.SharedPrefs.BackupRestore.name , appContext)
    }

    private fun clearUrisPermissions(context: Context){
        val contentResolver = context.contentResolver
        val persistedUriPermissions = contentResolver.persistedUriPermissions
        for (permission in persistedUriPermissions) {
            try {
                contentResolver.releasePersistableUriPermission(
                    permission.uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    private fun clearSharedPreference(name : String , context : Context){
        context.getSharedPreferences(name , Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
    @Given("^an user start the app$")
    fun anUserStartTheApp() {
        activityScenarioHolder.launch(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                MainActivity::class.java
            ))
    }

    @When("^the user input three new positive emotions$")
    fun theUserInputThreeNewPositiveEmotions() {
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

    @Then("^there is three list of card in home page$")
    fun thereIsThreeListOfCardInHomePage() {
        mainHomeRobot
            .waitForItemCount(itemCount = 3)
            .assertNthRecyclerViewTitle(0 , "saya senang")
            .assertNthRecyclerViewTitle(1 , "saya bersyukur")
            .assertNthRecyclerViewTitle(2 , "Saya terhibur")

    }

    @When("the user edit the second positive emotion")
    fun theUserEditTheSecondPositiveEmotion() {
        mainHomeRobot
            .toEditGratitudeWithNthItem(index = 1)
        editScreenRobot
            .replaceWhatValue("title berubah")
            .replaceWhyValue("\"\"\"\"")
            .pressBack()
    }

    @Then("the second positive emotion edited")
    fun theSecondPositiveEmotionEdited() {
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

    @When("the user navigate to backup screen")
    fun theUserNavigateToBackupScreen() {
        mainHomeRobot
            .navigateToBackupRestore()
    }

    @And("the user try to create a new backup")
    fun theUserTryToCreateANewBackup() {
        backupRestoreRobot.clickPilihLokasiBackup()
        // onNodeWithText("Pilih Lokasi Backup").performClick()
    }

    @Then("a new backup item is displayed")
    fun aNewBackupItemIsDisplayed() {
    }
}