package com.example.gratefulnote

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.fragment_controller.AddGratitudeController
import com.example.gratefulnote.fragment_controller.MainHomeController
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
        val mainHomeController = MainHomeController()
        val addGratitudeController = AddGratitudeController()

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


    }
}