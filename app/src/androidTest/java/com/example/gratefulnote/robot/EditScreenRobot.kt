package com.example.gratefulnote.robot

import com.example.gratefulnote.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

class EditScreenRobot : Backable() {
    fun replaceWhatValue(whatValue : String): EditScreenRobot {
        onView(withId(R.id.edit_positive_emotion_title_value))
            .perform(replaceText(whatValue))
        return this
    }

    fun replaceWhyValue(whyValue : String): EditScreenRobot {
        onView(withId(R.id.edit_positive_emotion_description_value))
            .perform(replaceText(whyValue))
        return this
    }

    fun assertWhatValue(whatValue: String): EditScreenRobot {
        onView(withId(R.id.edit_positive_emotion_title_value))
            .check(matches(withText(whatValue)))
        return this
    }

    fun assertWhyValue(whyValue: String): EditScreenRobot {
        onView(withId(R.id.edit_positive_emotion_description_value))
            .check(matches(withText(whyValue)))
        return this
    }
}