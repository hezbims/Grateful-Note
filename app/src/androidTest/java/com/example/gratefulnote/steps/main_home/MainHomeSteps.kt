package com.example.gratefulnote.steps.main_home

import com.example.gratefulnote.robot.main_home.MainHomeRobot
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class MainHomeSteps {
    private val mainHomeRobot = MainHomeRobot()

    @When("the user go to edit the {int}-th diary with title {string}")
    fun whenUserTryToEditNthData(n: Int, title: String){
        mainHomeRobot.diaryList.apply {
            val index = n - 1

            scrollToIndex(index)
            assertTitleAtIndex(title = title, index = index)
            getDiaryCardWithTitle(title)
                .editIcon
                .performClick()
        }
    }

    @Then("the {int}-th diary in main home is titled {string}")
    fun assertNthDiaryTitleInMainHome(n: Int, expectedTitle: String){
        val index = n - 1
        mainHomeRobot.diaryList.apply {
            waitUntilItemCountAtLeast(index + 1)
            scrollToIndex(index)
            assertTitleAtIndex(
                index = index,
                title = expectedTitle)
        }
    }

    @And("the {int}-th diary has tag {string}")
    fun assertNthDiaryEmotionTag(n: Int, expectedTag: String){
        val index = n - 1
        mainHomeRobot.diaryList.apply {
            waitUntilItemCountAtLeast(index + 1)
            scrollToIndex(index)
            assertTagAtIndex(
                index = index,
                tag = expectedTag
            )
        }
    }

    @When("the user click the delete icon at the {int}-th diary with title {string}")
    fun clickDeleteIcon(n: Int, title: String){
        mainHomeRobot.diaryList.apply {
            val index = n - 1

            scrollToIndex(index)
            assertTitleAtIndex(index = index, title = title)
            getDiaryCardWithTitle(title = title)
                .deleteIcon
                .performClick()
        }
    }

    @And("confirm the diary deletion")
    fun confirmTheDiaryDeletion(){
        mainHomeRobot.confirmDeleteDialog.confirmDeletion()
    }

    @When("user click add new diary button")
    fun clickAddNewDiaryButton(){
        mainHomeRobot.addNewDiaryButton.performClick()
    }
}