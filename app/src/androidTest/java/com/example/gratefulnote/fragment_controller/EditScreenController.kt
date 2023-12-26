package com.example.gratefulnote.fragment_controller

import com.example.gratefulnote.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

class EditScreenController : Backable() {
    fun replaceWhatValue(whatValue : String){
        onView(withId(R.id.edit_positive_emotion_title_value))
            .perform(replaceText(whatValue))
    }

    fun replaceWhyValue(whyValue : String){
        onView(withId(R.id.edit_positive_emotion_description_value))
            .perform(replaceText(whyValue))
    }

    fun assertWhatValue(whatValue: String){
        onView(withId(R.id.edit_positive_emotion_title_value))
            .check(matches(withText(whatValue)))
    }

    fun assertWhyValue(whyValue: String){
        onView(withId(R.id.edit_positive_emotion_description_value))
            .check(matches(withText(whyValue)))
    }
}