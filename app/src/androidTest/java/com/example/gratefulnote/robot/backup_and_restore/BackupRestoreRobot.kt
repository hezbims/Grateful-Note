package com.example.gratefulnote.robot.backup_and_restore

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.gratefulnote.robot._common.BaseRobot
import com.example.gratefulnote.robot._common.ComposeRuleHolder

@OptIn(ExperimentalTestApi::class)
class BackupRestoreRobot(
    composeRuleHolder: ComposeRuleHolder
) : BaseRobot(composeRuleHolder.composeRule) {
    fun clickPilihLokasiBackup(){
        composeRule.waitUntilExactlyOneExists(hasText("Pilih Lokasi Backup"))
        composeRule.onNodeWithText("Pilih Lokasi Backup").performClick()
    }
}