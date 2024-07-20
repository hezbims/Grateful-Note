package com.example.gratefulnote.robot.daily_reminder

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
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
    fun toogleSwitchOnItem(
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

    fun waitUntilSwitch(index: Int, isSwitchOff: Boolean) : DailyReminderRobot {
        composeRule.waitUntil {
            try {
                val targetSwitch = composeRule
                    .onNodeWithTag(DailyNotificationTestTag.listDailyNotificationLazyColumn)
                    .onChildren()[index].onChild()
                targetSwitch.assert((if (isSwitchOff) isOff() else isOn()))
                true
            } catch (e : AssertionError) {
                false
            }
        }
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
            timeoutMillis = 5000
        )

        return this
    }

    fun assertDailyReminderDisplayed(hour : Int, minute : Int, index: Int){
        composeRule.onNodeWithTag(DailyNotificationTestTag.listDailyNotificationLazyColumn)
            .onChildren()[index]
            .assertTextEquals(getClockString(hour = hour, minute = minute))
    }


    fun addNewAlarm(jarumJam: Int, jarumMenit: Int) : DailyReminderRobot {
        if (jarumMenit < 0 || jarumMenit > 11){
            throw Exception("Nilai menit harus berada di antara 0-11")
        }

        composeRule.onNodeWithContentDescription(
            appContext.getString(R.string.buat_daily_notification_baru)
        ).performClick()

        // ngeprint semantics tree dalam pemilihan hour di time picker
        // composeRule.onNodeWithTag(DailyNotificationTestTag.timePicker).printToLog("qqq")
        composeRule.onNodeWithContentDescription("$jarumJam hours").performClick()

        // ngeprint semantics tree dalam pemilihan menit di time picker
        // composeRule.onNodeWithTag(DailyNotificationTestTag.timePicker).printToLog("qqq")
        composeRule.onNodeWithContentDescription("${jarumMenit * 5} minutes").performClick()
        composeRule.onNodeWithText(appContext.getString(R.string.konfirmasi)).performClick()

        return this
    }

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
}