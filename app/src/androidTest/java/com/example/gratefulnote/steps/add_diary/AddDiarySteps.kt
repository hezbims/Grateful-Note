package com.example.gratefulnote.steps.add_diary

import com.example.gratefulnote.robot.add_new_diary.AddNewDiaryRobot
import io.cucumber.java.en.And

class AddDiarySteps {
    private val addNewDiaryRobot = AddNewDiaryRobot()

    @And("^add title with '(.*)'$")
    fun addDiaryTitle(title: String){
        addNewDiaryRobot.titleTextField.type(title)
    }

    @And("^add description with '(.*)'$")
    fun addDiaryDescription(description: String){
        addNewDiaryRobot.descTextField.type(description)
    }

    @And("^add tag with '(.*)'$")
    fun addDiaryTag(tag: String){
        addNewDiaryRobot.tagDropdown.chooseTag(tag)
    }

    @And("^click save new diary$")
    fun clickSaveNewDiaryIcon(){
        addNewDiaryRobot.saveButton.performClick()
    }

    @And("^confirm to save new diary$")
    fun confirmToSaveNewDiary(){
        addNewDiaryRobot.confirmSaveDialog.confirmButton.performClick()
    }
}