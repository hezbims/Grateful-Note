package com.example.gratefulnote.utils

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.gratefulnote.MainActivity

typealias MyComposeActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>