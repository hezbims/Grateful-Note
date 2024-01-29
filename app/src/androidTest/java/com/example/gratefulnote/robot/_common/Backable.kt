package com.example.gratefulnote.robot._common

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers

abstract class Backable(composeRuleHolder: ComposeRuleHolder)
    : BaseRobot(composeRuleHolder.composeRule){
    fun pressBack(){
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
    }
}