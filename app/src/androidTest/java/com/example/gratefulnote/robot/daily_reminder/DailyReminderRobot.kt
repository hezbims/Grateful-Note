package com.example.gratefulnote.robot.daily_reminder

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import com.example.gratefulnote.daily_notification.test_tag.DailyNotificationTestTag

class DailyReminderRobot(
    private val composeRule : ComposeTestRule
) {
    fun toogleSwitchOnNthItem(
        hour : Int,
        minute : Int,
    ) : DailyReminderRobot {
        val paddedHour = hour.toString().padStart(2 , '0')
        val paddedMinute = minute.toString().padStart(2 , '0')

        val targetListItemCard = hasAnyDescendant(
            hasText("$paddedHour:$paddedMinute")
        ) and hasTestTag(DailyNotificationTestTag.listItemCard)

        val allListItemSwitches = hasTestTag(DailyNotificationTestTag.listItemSwitch)

        val targetSwitch = allListItemSwitches and hasParent(targetListItemCard)

        composeRule.onNode(targetSwitch).performClick()

        return this
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitUntilItemCount(itemCount : Int) : DailyReminderRobot {
        composeRule.waitUntilNodeCount(
            matcher = hasTestTag(DailyNotificationTestTag.listItemCard),
            count = itemCount
        )
        return this
    }
}