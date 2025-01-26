package com.example.gratefulnote.steps.common

import com.example.gratefulnote.steps.common.rule.BaseInstrumentedTestRule
import com.example.gratefulnote.test_scenario._seeder.DiarySeeder
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidTest
class AppStartupSteps(private val startupRule: BaseInstrumentedTestRule) {
    @Inject
    lateinit var diarySeeder : DiarySeeder


    @Given("^the user have \"([^\"]*)\" minimal diaries$")
    fun theUserHaveDiaries(totalDiaries: Int) = runBlocking {
        diarySeeder.executeMinimal(totalDiaries)
    }

    @And("user start the app")
    fun userStartTheApp(){
        startupRule.startMainActivity()
    }
}