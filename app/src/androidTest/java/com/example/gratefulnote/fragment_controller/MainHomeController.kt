package com.example.gratefulnote.fragment_controller

import com.example.gratefulnote.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId

class MainHomeController {
    fun toAddGratitude(){
        onView(withId(R.id.add_new_gratitude))
            .perform(click())
    }
}