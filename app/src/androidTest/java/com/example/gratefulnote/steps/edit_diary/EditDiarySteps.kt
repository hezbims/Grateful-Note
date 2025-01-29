package com.example.gratefulnote.steps.edit_diary

import com.example.gratefulnote.robot.edit_screen.EditScreenRobot
import com.example.gratefulnote.robot.main_home.MainHomeRobot
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.en.And
import io.cucumber.java.en.Then

@HiltAndroidTest
class EditDiarySteps {
    private val editDiaryRobot = EditScreenRobot()
    private val mainHomeRobot = MainHomeRobot()

    @And("^edit title with '(.*)'$")
    fun editTitleWith(newTitle: String){
        editDiaryRobot.titleTextField.apply {
            clear()
            type(newTitle)
        }
    }

    @And("^edit desc with '(.*)'$")
    fun editDescWith(newDesc: String){
        editDiaryRobot.descTextField.apply {
            clear()
            type(newDesc)
        }
    }

    @And("^user go back after edit the diary$")
    fun goBackAfterEditDiary(){
        editDiaryRobot.pressBack()
        mainHomeRobot.waitUntilScreenAppear()
    }

    @Then("^the diary title in edit screen is '(.*)'$")
    fun assertDiaryTitle(expectedTitle: String){
        editDiaryRobot.titleTextField.assertContent(expectedTitle)
    }

    @And("^the diary description in edit screen is '(.*)'$")
    fun assertDiaryDescription(expectedDescription: String){
        editDiaryRobot.descTextField.assertContent(expectedDescription)
    }
}