package com.example.gratefulnote.steps.main_home

import com.example.gratefulnote.robot.main_home.MainHomeRobot
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class MainHomeSteps {
    private val mainHomeRobot = MainHomeRobot()

    @When("^the user try edit the '(.*)'-th diary with title '(.*)'$")
    fun whenUserTryToEditNthData(n: String, title: String){
        mainHomeRobot.diaryList.apply {
            val index = n.toInt() - 1

            scrollToIndex(index)
            assertTitleAtIndex(title = title, index = index)
            getDiaryCardWithTitle(title)
                .editIcon
                .performClick()
        }
    }

    @Then("^the '(.*)'-th diary in main home is titled '(.*)'$")
    fun assertNthDiaryTitleInMainHome(n: String, expectedTitle: String){
        val index = n.toInt() - 1
        mainHomeRobot.diaryList.apply {
            waitUntilItemCountAtLeast(index + 1)
            scrollToIndex(index)
            assertTitleAtIndex(
                index = index,
                title = expectedTitle)
        }
    }

    @When("^the user click the delete icon at the '(.*)'-th diary with title '(.*)'$")
    fun clickDeleteIcon(n: String, title: String){
        mainHomeRobot.diaryList.apply {
            val index = n.toInt() - 1

            scrollToIndex(index)
            assertTitleAtIndex(index = index, title = title)
            getDiaryCardWithTitle(title = title)
                .deleteIcon
                .performClick()
        }
    }

    @And("^confirm the diary deletion$")
    fun confirmTheDiaryDeletion(){
        mainHomeRobot.confirmDeleteDialog.confirmDeletion()
    }
}