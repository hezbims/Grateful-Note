package com.example.gratefulnote.fragment_controller

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import org.hamcrest.CoreMatchers.allOf

class MainHomeController {
    fun toAddGratitude(){
        onView(withId(R.id.add_new_gratitude))
            .perform(click())
    }

    fun toEditGratitude(whatValue : String){
        val editButton = onView(allOf(
            withId(R.id.edit_positive_emotion),

            hasSibling(
                withChild(
                    withText(whatValue)
                )
            )
        ))
        editButton.perform(click())
    }
}