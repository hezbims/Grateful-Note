package com.example.gratefulnote.robot._common_utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers.any

class WaitViewUntil(
    /// contoh : View.GONE
    private val condition : (View) -> Boolean,
    private val timeoutInMillis : Long = 5000L
) : ViewAction {
    override fun getDescription(): String {
        return "Wait for view with condition"
    }

    override fun getConstraints(): Matcher<View> {
        return any(View::class.java)
    }

    override fun perform(uiController: UiController, view: View) {
        val endtime = System.currentTimeMillis() + timeoutInMillis
        do {
            if (condition(view)) return
            uiController.loopMainThreadForAtLeast(100L)
        } while (System.currentTimeMillis() < endtime)

        throw RuntimeException("Timeout ketika coba waiting view")
    }

}