package com.example.gratefulnote.robot.diary_filter_dialog.components

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.IEspressoComponent

interface IOnlyFavoriteFilterSwitch {
    fun toogle()
}

class OnlyFavoriteFilterSwitch : IEspressoComponent, IOnlyFavoriteFilterSwitch {
    override val viewInteractor: ViewInteraction
        get() = onView(withId(R.id.is_only_favorite_switch))

    override fun toogle() {
        viewInteractor.perform(click())
    }
}