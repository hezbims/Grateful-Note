package com.example.gratefulnote.robot.diary_filter_dialog.components

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.robot._common.IEspressoComponent

interface IApplyFilterButton {
    fun tap()
}

class ApplyFilterButton : IEspressoComponent, IApplyFilterButton {
    override val viewInteractor: ViewInteraction
        get() = onView(withText("TERAPKAN"))

    override fun tap(){
        viewInteractor.perform(click())
    }
}