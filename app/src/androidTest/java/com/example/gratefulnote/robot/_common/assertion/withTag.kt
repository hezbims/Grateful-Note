package com.example.gratefulnote.robot._common.assertion

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun withTag(expectedTag: Any) : TypeSafeMatcher<View>{
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(item: View?): Boolean {
            return item?.tag == expectedTag
        }

        override fun describeTo(description: Description?) {
            description?.appendText("Memiliki ekspetasi tag : $expectedTag")
        }
    }
}