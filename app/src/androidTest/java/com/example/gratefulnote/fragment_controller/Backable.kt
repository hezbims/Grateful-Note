package com.example.gratefulnote.fragment_controller

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers

public abstract class Backable {
    fun pressBack(){
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
    }
}