package com.example.gratefulnote.robot._common_utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

class ClickRecyclerViewItemAction {
    companion object {
        fun clickChildWithId(
            id : Int
        ): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Mengclick recyclerview item dengan id : $id"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    val itemClicked = view?.findViewById<View>(id)
                    itemClicked?.performClick()
                }
            }
        }

    }
}