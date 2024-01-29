package com.example.gratefulnote.steps_definition

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.database.PositiveEmotionDatabase
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
//        val mainHomeRobot = MainHomeRobot()
//        val addGratitudeRobot = AddGratitudeRobot()
//        val editScreenRobot = EditScreenRobot()
//
//        mainHomeRobot.toAddGratitude()
//        addGratitudeRobot.fillFormAndSave(
//            spinnerValue = "Amusement",
//            whatValue = "Saya terhibur",
//            whyValue = "karena diajak berwisata"
//        )
//
//        mainHomeRobot.toAddGratitude()
//        addGratitudeRobot.fillFormAndSave(
//            spinnerValue = "Gratitude",
//            whatValue = "saya bersyukur",
//            whyValue = "karena lulus ujian"
//        )
//
//        mainHomeRobot.toAddGratitude()
//        addGratitudeRobot.fillFormAndSave(
//            spinnerValue = "Joy",
//            whatValue = "saya senang",
//            whyValue = "Saya senang karena bermain sepak bola di hari ini"
//        )
//
//        /// nyoba ngedit salah satu gratitude
//        mainHomeRobot.toEditGratitudeWithTitle(title = "saya bersyukur")
//        editScreenRobot
//            .replaceWhatValue(whatValue = "saya")
//            .replaceWhyValue(whyValue = "bersyukur")
//            .pressBack()
//
//        // mastiin proses editing berhasil
//        mainHomeRobot.toEditGratitudeWithNthItem(index = 1)
//        editScreenRobot
//            .assertWhatValue(whatValue = "saya")
//            .assertWhyValue(whyValue = "bersyukur")
//            .pressBack()

    }
}