package com.example.gratefulnote.steps.main_home

import com.example.gratefulnote.robot.main_home.MainHomeRobot
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
        mainHomeRobot.assertNthRecyclerViewTitle(
            itemIndex = index,
            title = expectedTitle)
    }
}