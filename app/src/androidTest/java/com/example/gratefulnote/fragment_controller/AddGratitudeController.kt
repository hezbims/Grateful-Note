package com.example.gratefulnote.fragment_controller

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R

class AddGratitudeController {
    fun enterWhyText(value : String){
        onView(withId(R.id.why_value))
            .perform(typeText(value))
    }

    fun enterWhatText(value: String){
        onView(withId(R.id.what_value))
            .perform(typeText(value))
    }

    fun chooseSpinnerItemWithText(value : String){
        onView(withId(R.id.add_gratitude_spinner))
            .perform(ViewActions.click())
        onView(ViewMatchers.withText(value))
            .perform(ViewActions.click())
    }

    fun confirmSave(){
        onView(withId(R.id.save))
            .perform(ViewActions.click())

        onView(withText("YA"))
            .perform(ViewActions.click())
    }

    fun fillFormAndSave(
        spinnerValue : String,
        whatValue : String,
        whyValue : String
    ){
        chooseSpinnerItemWithText(spinnerValue)
        enterWhatText(whatValue)
        enterWhyText(whyValue)
        confirmSave()
    }
}