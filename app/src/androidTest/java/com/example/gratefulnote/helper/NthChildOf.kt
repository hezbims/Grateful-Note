package com.example.gratefulnote.helper

import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun nthChildOf(parentMatcher : Matcher<View>, index : Int) : TypeSafeMatcher<View>{
    return object : TypeSafeMatcher<View>(){
        override fun describeTo(description: Description?) {
            description?.appendText("deskripsi nth child")
        }

        // item : iterate semua view
        override fun matchesSafely(item: View?): Boolean {
            if (item?.parent !is ViewGroup)
                return parentMatcher.matches(item?.parent)

            val parentViewGroup = item.parent as ViewGroup
            return parentMatcher.matches(item.parent) &&
                    parentViewGroup.getChildAt(index).equals(item)
        }
    }
}