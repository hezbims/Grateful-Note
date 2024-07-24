package com.example.gratefulnote.robot._common

import com.example.gratefulnote.utils.MyComposeActivityRule

abstract class Backable(
    private val composeRule : MyComposeActivityRule
) {
    fun pressBack(){
        composeRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
    }
}