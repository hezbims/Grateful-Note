package com.example.gratefulnote.robot.backup_and_restore

import android.content.Intent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.example.gratefulnote.backuprestore.presentation.test_tag.BackupRestoreNodeTag
import com.example.gratefulnote.robot._common.Backable
import com.example.gratefulnote.utils.MyComposeActivityRule
import com.example.gratefulnote.utils.waitUntilSucceed
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalTestApi::class)
class BackupRestoreRobot(
    private val composeRule : MyComposeActivityRule
) : Backable(composeRule) {
    fun clickPilihLokasiBackup() : BackupRestoreRobot{
        composeRule.waitUntilExactlyOneExists(hasText("Pilih Lokasi Backup"))
        composeRule.onNodeWithText("Pilih Lokasi Backup").performClick()
        runBlocking {
            waitUntilSucceed {
                Intents.intended(
                    IntentMatchers.hasAction(Intent.ACTION_OPEN_DOCUMENT_TREE),
                )
            }
        }
        composeRule.waitUntilExactlyOneExists(hasTestTag(BackupRestoreNodeTag.bottomActionCard))

        return this
    }

    fun clickDanBuatBackupBaru(namaBackup : String) : BackupRestoreRobot{
        composeRule
            .onNodeWithText("Buat Backup Baru")
            .performClick()
        composeRule
            .onNodeWithTag(BackupRestoreNodeTag.buatBackupBaruTextField)
            .performTextInput(namaBackup)
        composeRule
            .onNodeWithText("Buat")
            .performClick()
        composeRule.waitUntilDoesNotExist(hasTestTag(BackupRestoreNodeTag.buatBackupBaruDialog))

        return this
    }

    fun assertFileBackupExist(backupTitle : String) : BackupRestoreRobot {
        composeRule
            .onNodeWithTag(BackupRestoreNodeTag.backupFilesLazyColumn)
            .performScrollToNode(hasText(backupTitle))

        return this
    }

    fun restoreBackupFile(backupTitle: String) : BackupRestoreRobot {
        composeRule
            .waitUntilExactlyOneExists(hasTestTag(BackupRestoreNodeTag.backupFilesLazyColumn))

        val listItemMatcher = hasTestTag(BackupRestoreNodeTag.fileListItem) and hasAnyDescendant(
            hasText(backupTitle)
        )

        composeRule
            .onNodeWithTag(BackupRestoreNodeTag.backupFilesLazyColumn)
            .performScrollToNode(listItemMatcher)

        composeRule.onNode(
            hasText("Restore") and
            hasAnyAncestor(listItemMatcher)
        ).performClick()

        composeRule.onNodeWithText("YA").performClick()
        composeRule.waitUntilDoesNotExist(
            hasTestTag(BackupRestoreNodeTag.confirmRestoreDialog)
        )

        return this
    }
}