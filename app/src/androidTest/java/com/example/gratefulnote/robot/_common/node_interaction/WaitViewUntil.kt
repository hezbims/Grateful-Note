package com.example.gratefulnote.robot._common.node_interaction

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers.any

class WaitViewUntil(
    /// contoh : View.GONE
    private val timeoutInMillis : Long = 2500L,
    private val tag: String? = null,
    private val condition : (View?) -> Boolean,
) : ViewAction {
    override fun getDescription(): String {
        return "Wait for view with condition"
    }

    override fun getConstraints(): Matcher<View> {
        return any(View::class.java)
    }

    override fun perform(uiController: UiController, view: View?) {
        val endtime = System.currentTimeMillis() + timeoutInMillis
        do {
            try {
                if (condition(view)) return
            } catch (_: Throwable){}
            uiController.loopMainThreadForAtLeast(100L)
        } while (System.currentTimeMillis() < endtime)


        throw RuntimeException("Timeout ketika coba waiting view : $tag")
    }

}