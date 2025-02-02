package com.example.gratefulnote.robot._common.interactor.base

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.example.gratefulnote.robot._common.assertion.withTag
import com.example.gratefulnote.utils.waitUntilSucceed
import kotlinx.coroutines.runBlocking
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

    fun assertTag(tag: Any) = runBlocking {
        waitUntilSucceed {
            onView(viewMatcher).check(
                matches(
                    withTag(tag)
                )
            )
        }
    }

    fun performClick(){
        onView(viewMatcher).perform(click())
    }
}