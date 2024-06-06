package com.example.gratefulnote.robot._common.node_interaction

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun nthChildOf(matcher : Matcher<View>, index : Int) : BoundedMatcher<View , RecyclerView>{
    return object : BoundedMatcher<View , RecyclerView>(RecyclerView::class.java){
        override fun describeTo(description: Description?) {
            description?.appendText("finding nth child of recyclerview")
        }


        override fun matchesSafely(recyclerView: RecyclerView): Boolean {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(index)!!
            return matcher.matches(viewHolder.itemView)
        }
    }
}