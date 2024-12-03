package com.example.gratefulnote.robot.main_home

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.node_interaction.ClickRecyclerViewItemAction
import com.example.gratefulnote.robot._common.node_interaction.WaitViewUntil
import com.example.gratefulnote.utils.waitUntilSucceed
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf

class MainHomeRobot {
    fun toAddGratitude() : MainHomeRobot {
        onView(withId(R.id.add_new_gratitude_action_icon))
            .perform(click())
        return this
    }

    fun toEditGratitudeWithTitle(title : String) : MainHomeRobot {
        val editButton = onView(allOf(
            withId(R.id.edit_positive_emotion),

            hasSibling(
                withChild(
                    withText(title)
                )
            )
        ))
        editButton.perform(click())
        return this
    }

    /**
     * Mencet tombol edit di item index ke-n
     */
    fun toEditGratitudeWithNthItem(index : Int) : MainHomeRobot {
        onView(
            withId(R.id.recyclerView)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(
                index,
                ClickRecyclerViewItemAction.clickChildWithId(
                    R.id.edit_positive_emotion
                )
            )
        )
        return this
    }

    fun assertNthRecyclerViewTitle(itemIndex : Int, title: String) : MainHomeRobot{
        val matcher = hasDescendant(withText(title))

        val recycleView = onView(withId(R.id.recyclerView))
        recycleView.perform(WaitViewUntil(
            condition = { view ->
                val rView = view as RecyclerView
                val viewHolder = rView.findViewHolderForAdapterPosition(itemIndex)
                matcher.matches(viewHolder?.itemView)
            },
            tag = title
        ))
        return this
    }

    fun waitForItemCount(itemCount : Int) : MainHomeRobot{
        val recycleView = onView(withId(R.id.recyclerView))
        recycleView.perform(WaitViewUntil(
            condition = { view ->
                val rView = view as RecyclerView
                rView.adapter?.itemCount == itemCount
            }
        ))
        return this
    }

    fun deleteNthPositiveEmotion(index : Int) : MainHomeRobot{
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(
                    index,
                    ClickRecyclerViewItemAction.clickChildWithId(R.id.delete_positive_emotion)
                )
            )
        runBlocking {
            waitUntilSucceed {
                onView(withText(R.string.confirm_delete_message)).check(
                    ViewAssertions.matches(isDisplayed())
                )
            }
        }
        onView(withText("YA"))
            .perform(click())

        return this
    }

    fun openFilterMenu(){
        onView(withId(R.id.filter_list_positive_emotion_action_icon))
            .perform(click())
    }

    val navBar = MainNavBarRobot()
    inner class MainNavBarRobot {
        fun toBackupRestore() : MainHomeRobot{
            onView(withId(R.id.drawer_layout))
                .check(ViewAssertions.matches(DrawerMatchers.isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
            onView(withId(R.id.backupRestoreFragment))
                .perform(click())

            return this@MainHomeRobot
        }

        fun toDailyReminder() : MainHomeRobot {
            onView(withId(R.id.drawer_layout))
                .check(ViewAssertions.matches(DrawerMatchers.isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
            onView(withId(R.id.notificationSettingsFragment))
                .perform(click())

            return this@MainHomeRobot
        }
    }



}