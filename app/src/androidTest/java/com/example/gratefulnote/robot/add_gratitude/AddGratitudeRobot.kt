package com.example.gratefulnote.robot.add_gratitude

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R

class AddGratitudeRobot {
    fun enterWhyText(value : String): AddGratitudeRobot {
        onView(withId(R.id.why_value))
            .perform(typeText(value))
        return this
    }

    fun enterWhatText(value: String): AddGratitudeRobot {
        onView(withId(R.id.what_value))
            .perform(typeText(value))
        return this
    }

    fun chooseSpinnerItemWithText(value : String): AddGratitudeRobot {
        onView(withId(R.id.add_gratitude_spinner))
            .perform(ViewActions.click())
        onView(withText(value))
            .perform(ViewActions.click())
        return this
    }

    fun confirmSave(): AddGratitudeRobot {
        onView(withId(R.id.save))
            .perform(ViewActions.click())

        onView(withText("YA"))
            .perform(ViewActions.click())
        return this
    }

    fun fillFormAndSave(
        spinnerValue : String,
        whatValue : String,
        whyValue : String
    ): AddGratitudeRobot {
        chooseSpinnerItemWithText(spinnerValue)
        enterWhatText(whatValue)
        enterWhyText(whyValue)
        confirmSave()
        return this
    }
}