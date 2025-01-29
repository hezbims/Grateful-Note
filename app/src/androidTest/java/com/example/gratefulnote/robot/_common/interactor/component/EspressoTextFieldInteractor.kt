package com.example.gratefulnote.robot._common.interactor.component

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.robot._common.interactor.base.EspressoInteractor
import org.hamcrest.Matcher

class EspressoTextFieldInteractor(matcher: Matcher<View>) : EspressoInteractor(matcher) {
    fun type(text: String){
        onView(viewMatcher).perform(typeText(text), closeSoftKeyboard())
    }

    fun clear(){
        onView(viewMatcher).perform(ViewActions.replaceText(""))
    }

    fun assertContent(expectedText: String){
        onView(viewMatcher).check(matches(withText(expectedText)))
    }
}