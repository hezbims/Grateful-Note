package com.example.gratefulnote.robot.diary_filter_dialog.components

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.gratefulnote.R

class MonthDropdown : EspressoFilterDropdownComponent() {
    override val viewInteractor: ViewInteraction
        get() = onView(withId(R.id.month_spinner))
}