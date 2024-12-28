package com.example.gratefulnote.robot._common

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import org.hamcrest.Matcher

abstract class EspressoInteractor(protected val viewMatcher: Matcher<View>) {
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
}