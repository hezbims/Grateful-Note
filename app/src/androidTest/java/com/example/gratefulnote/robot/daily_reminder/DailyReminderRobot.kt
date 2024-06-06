package com.example.gratefulnote.robot.daily_reminder

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.R
import com.example.gratefulnote.daily_notification.test_tag.DailyNotificationTestTag
import com.example.gratefulnote.robot._common.node_interaction.isNotCached

class DailyReminderRobot(
    private val composeRule : ComposeTestRule
) {
    fun toogleSwitchOnNthItem(
        hour : Int,
        minute : Int,
    ) : DailyReminderRobot {
        val clockString = getClockString(hour = hour , minute = minute)

        val targetListItemCard = hasAnyDescendant(hasText(clockString)) and
            hasTestTag(DailyNotificationTestTag.listItemCard)
        composeRule.onNode(targetListItemCard, useUnmergedTree = true).assertExists()

        val allListItemSwitches = hasTestTag(DailyNotificationTestTag.listItemSwitch)
        val targetSwitch = allListItemSwitches and hasParent(targetListItemCard)
        composeRule.onNode(targetSwitch, useUnmergedTree = true).performClick()

        return this
    }

    fun longClickOnListItem(hour: Int, minute: Int) : DailyReminderRobot {
        val clockString = getClockString(hour = hour , minute = minute)

        val targetItem = hasText(clockString)
        composeRule.onNode(targetItem).performTouchInput { longClick() }

        return this
    }

    fun toogleCheckbox(hour: Int, minute: Int) : DailyReminderRobot {
        val clockString = getClockString(hour = hour , minute = minute)

        val targetCard = hasText(clockString)

        composeRule.onNode(targetCard).performClick()

        return this
    }

    fun performDeleteSelectedItem() : DailyReminderRobot {
        composeRule.onNodeWithContentDescription(
            appContext.getString(R.string.hapus_item_dipilih)
        ).performClick()

        composeRule.onNodeWithText("YA").performClick()

        return this
    }

    private fun getClockString(hour: Int, minute: Int) : String {
        val paddedHour = hour.toString().padStart(2 , '0')
        val paddedMinute = minute.toString().padStart(2 , '0')
        return "$paddedHour:$paddedMinute"
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitUntilItemCount(expectedCount : Int) : DailyReminderRobot {
        composeRule.waitUntilNodeCount(
            matcher = hasTestTag(DailyNotificationTestTag.listItemCard) and isNotCached(),
            count = expectedCount,
        )

        return this
    }

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
}