package com.example.gratefulnote.fragment_controller

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import com.example.gratefulnote.helper.ClickRecyclerViewItemAction
import org.hamcrest.CoreMatchers.allOf

class MainHomeController {
    fun toAddGratitude(){
        onView(withId(R.id.add_new_gratitude))
            .perform(click())
    }

    fun toEditGratitudeWithTitle(title : String){
        val editButton = onView(allOf(
            withId(R.id.edit_positive_emotion),

            hasSibling(
                withChild(
                    withText(title)
                )
            )
        ))
        editButton.perform(click())
    }

    /**
     * Mencet tombol edit di item index ke-n
     */
    fun toEditGratitudeWithNthItem(index : Int){
        val editButton = onView(
            withId(R.id.recyclerView)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(
                index,
                ClickRecyclerViewItemAction.clickChildWithId(
                    R.id.edit_positive_emotion
                )
            )
        )
    }


}