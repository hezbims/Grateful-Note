package com.example.gratefulnote.robot.add_new_diary.component

import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.example.gratefulnote.robot._common.interactor.base.EspressoInteractor
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`

class TagDropdown(viewMatcher: Matcher<View>) : EspressoInteractor(viewMatcher) {
    fun chooseTag(tag: String){
        onView(viewMatcher).perform(click())
        onData(allOf(`is`(tag), `is`(instanceOf(String::class.java))))
            .perform(click())
    }
}