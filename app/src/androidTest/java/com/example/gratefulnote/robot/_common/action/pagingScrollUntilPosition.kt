package com.example.gratefulnote.robot._common.action

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers.instanceOf
import kotlin.math.min

fun pagingScrollUntilPosition(
    targetIndex: Int,
    pagingLoadTimeOut: Long = 2000L
) : ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Men-scroll hingga ke posisi tertentu pada recycler view dengan pagination"
        }

        override fun getConstraints(): Matcher<View> {
            return instanceOf(RecyclerView::class.java)
        }

        override fun perform(uiController: UiController?, recyclerView: View?) {
            if (recyclerView !is RecyclerView)
                throw IllegalArgumentException("Hanya bisa di perform pada recycler view")

            do {
                val scrollPosition = min(
                    recyclerView.adapter?.itemCount?.minus(1) ?: 0,
                    targetIndex
                )

                recyclerView.scrollToPosition(scrollPosition)
                if (scrollPosition == targetIndex)
                    break

                val endTime = System.currentTimeMillis() + pagingLoadTimeOut
                while (
                    recyclerView.hasNotLoadedPagingDataAfterPosition(scrollPosition) &&
                    System.currentTimeMillis() < endTime
                )
                    uiController?.loopMainThreadForAtLeast(100L)


                if (recyclerView.hasNotLoadedPagingDataAfterPosition(scrollPosition))
                    throw RuntimeException("Loading paging terlalu lama, posisi terakhir scroll : $scrollPosition")

            } while (true)
        }
    }
}

private fun RecyclerView.hasNotLoadedPagingDataAfterPosition(position: Int) : Boolean {
    return adapter == null || adapter!!.itemCount - 1 <= position
}