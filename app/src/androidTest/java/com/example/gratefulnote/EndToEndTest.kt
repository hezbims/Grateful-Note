package com.example.gratefulnote

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.robot.AddGratitudeRobot
import com.example.gratefulnote.robot.EditScreenRobot
import com.example.gratefulnote.robot.MainHomeRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class EndToEndTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun clearDatabase(){
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .deleteDatabase(PositiveEmotionDatabase.dbName)
    }

    @Test
    fun end_To_End_Test(){
        val mainHomeController = MainHomeRobot()
        val addGratitudeController = AddGratitudeRobot()
        val editScreenController = EditScreenRobot()

        mainHomeController.toAddGratitude()
        addGratitudeController.fillFormAndSave(
            spinnerValue = "Amusement",
            whatValue = "Saya terhibur",
            whyValue = "karena diajak berwisata"
        )

        mainHomeController.toAddGratitude()
        addGratitudeController.fillFormAndSave(
            spinnerValue = "Gratitude",
            whatValue = "saya bersyukur",
            whyValue = "karena lulus ujian"
        )

        mainHomeController.toAddGratitude()
        addGratitudeController.fillFormAndSave(
            spinnerValue = "Joy",
            whatValue = "saya senang",
            whyValue = "Saya senang karena bermain sepak bola di hari ini"
        )

        /// nyoba ngedit salah satu gratitude
        mainHomeController.toEditGratitudeWithTitle(title = "saya bersyukur")
        editScreenController
            .replaceWhatValue(whatValue = "saya")
            .replaceWhyValue(whyValue = "bersyukur")
            .pressBack()

        // mastiin proses editing berhasil
        mainHomeController.toEditGratitudeWithNthItem(index = 1)
        editScreenController
            .assertWhatValue(whatValue = "saya")
            .assertWhyValue(whyValue = "bersyukur")
            .pressBack()

    }
}