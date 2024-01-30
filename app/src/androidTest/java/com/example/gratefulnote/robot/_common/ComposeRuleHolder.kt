package com.example.gratefulnote.robot._common

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.gratefulnote.MainActivity
import io.cucumber.junit.WithJunitRule
import org.junit.Rule

@WithJunitRule
class ComposeRuleHolder {
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()
}