package com.example.gratefulnote.robot._common_utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun nthChildOf(parentMatcher : Matcher<View>, index : Int) : BoundedMatcher<View , RecyclerView>{
    return object : BoundedMatcher<View , RecyclerView>(RecyclerView::class.java){
        override fun describeTo(description: Description?) {
            description?.appendText("deskripsi nth child")
        }


        override fun matchesSafely(recyclerView: RecyclerView?): Boolean {
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(index)
            return viewHolder != null && parentMatcher.matches(viewHolder.itemView)
        }
    }
}