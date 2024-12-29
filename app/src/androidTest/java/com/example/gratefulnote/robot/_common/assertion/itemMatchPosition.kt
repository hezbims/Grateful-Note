package com.example.gratefulnote.robot._common.assertion

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import org.hamcrest.Matcher

/**
 * Gunakan untuk assertion pada espresso recycler view
 */
fun itemMatchAtIndex(matcher: Matcher<View>, index: Int) : ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null)
            throw noViewFoundException

        if (view !is RecyclerView)
            throw IllegalArgumentException("view bukan recyclerview")

        val viewHolder = view.findViewHolderForAdapterPosition(index)
            ?: throw IllegalArgumentException("view holder pada posisi $index tidak ditemukan")

        matcher.matches(viewHolder)
    }
}