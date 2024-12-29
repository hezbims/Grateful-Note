package com.example.gratefulnote.robot._common.interactor.base

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import org.hamcrest.Matcher

open class EspressoInteractor(protected val viewMatcher: Matcher<View>) {
    fun assertGone(){
        viewMatcher.matches(
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        )
    }

    fun assertVisible(){
        viewMatcher.matches(
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        )
    }

    fun performClick(){
        onView(viewMatcher).perform(click())
    }
}