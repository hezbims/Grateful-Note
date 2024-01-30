package com.example.gratefulnote.robot.backup_and_restore

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.robot._common.BaseRobot

@OptIn(ExperimentalTestApi::class)
class BackupRestoreRobot(
    private val composeRule : AndroidComposeTestRule<ActivityScenarioRule<MainActivity> , MainActivity>
) : BaseRobot() {
    fun clickPilihLokasiBackup(){
        composeRule.waitUntilExactlyOneExists(hasText("Pilih Lokasi Backup"))
        composeRule.onNodeWithText("Pilih Lokasi Backup").performClick()

    }
}