package com.example.gratefulnote.steps_definition

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.helper.ActivityScenarioHolder
import com.example.gratefulnote.robot.add_gratitude.AddGratitudeRobot
import com.example.gratefulnote.robot.edit_screen.EditScreenRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class E2ETestSteps(
    private val mainHomeRobot: MainHomeRobot,
    private val addGratitudeRobot: AddGratitudeRobot,
    private val editScreenRobot: EditScreenRobot,
    private val activityScenarioHolder: ActivityScenarioHolder,
) {

    @Before("@e2e-test")
    fun clearDatabase(){
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .deleteDatabase(PositiveEmotionDatabase.dbName)
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
}