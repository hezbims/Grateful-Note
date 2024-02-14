package com.example.gratefulnote.robot.main_home

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.utils.ClickRecyclerViewItemAction
import com.example.gratefulnote.robot._common.utils.WaitViewUntil
import org.hamcrest.CoreMatchers.allOf

class MainHomeRobot {
    fun toAddGratitude() : MainHomeRobot {
        onView(withId(R.id.add_new_gratitude))
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
            }
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

    fun toBackupRestore() : MainHomeRobot{
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.backupRestoreFragment))
            .perform(click())

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
        onView(withText("YA"))
            .perform(click())

        return this
    }

}