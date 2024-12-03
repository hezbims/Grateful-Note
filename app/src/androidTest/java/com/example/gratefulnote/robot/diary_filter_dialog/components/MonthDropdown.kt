package com.example.gratefulnote.robot.diary_filter_dialog.components

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.IEspressoComponent
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`

interface IMonthDropdown {
    fun chooseMonth(month : String)
}

class MonthDropdown : IEspressoComponent, IMonthDropdown {
    override val viewInteractor: ViewInteraction
        get() = onView(withId(R.id.month_spinner))

    override fun chooseMonth(month : String){
        viewInteractor.perform(click())
        onData(
            allOf(
                `is`(instanceOf(String::class.java)),
                withSpinnerText(containsString(month))
            )
        ).perform(click())
    }
}