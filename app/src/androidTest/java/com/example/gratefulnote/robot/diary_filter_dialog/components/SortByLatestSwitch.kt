package com.example.gratefulnote.robot.diary_filter_dialog.components

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.IEspressoComponent

interface ISortByLatestSwitch {
    fun toogle()
}

class SortByLatestSwitch : IEspressoComponent, ISortByLatestSwitch {
    override val viewInteractor: ViewInteraction
        get() = onView(withId(R.id.is_sorted_latest_switch))

    override fun toogle(){
        viewInteractor.perform(click())
    }
}