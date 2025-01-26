package com.example.gratefulnote.steps.common.rule

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import com.example.gratefulnote.MainActivity
import io.cucumber.junit.WithJunitRule
import org.junit.Rule

@WithJunitRule(value = "@base_rule")
class BaseInstrumentedTestRule {
    @get:Rule(order = 999)
    val composeRule : ComposeTestRule = createEmptyComposeRule()
    private lateinit var scenario: ActivityScenario<MainActivity>

    fun startMainActivity(){
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }
}