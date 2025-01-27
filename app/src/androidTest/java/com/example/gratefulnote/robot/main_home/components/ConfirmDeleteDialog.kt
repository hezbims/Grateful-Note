package com.example.gratefulnote.robot.main_home.components

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText

class ConfirmDeleteDialog {
    fun confirmDeletion(){
        onView(withText("YA")).perform(click())
    }
}