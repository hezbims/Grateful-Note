package com.example.gratefulnote.robot.backup_and_restore

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.core.net.toUri
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.backuprestore.presentation.test_tag.BackupRestoreNodeTag
import com.example.gratefulnote.robot._common.BaseRobot

@OptIn(ExperimentalTestApi::class)
class BackupRestoreRobot(
    private val composeRule : AndroidComposeTestRule<ActivityScenarioRule<MainActivity> , MainActivity>
) : BaseRobot() {
    fun clickPilihLokasiBackup() : BackupRestoreRobot{
        val directoryUri = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .filesDir.toUri()

        val intentResult = Intent().apply {
            data = directoryUri
        }
        val intentMatcher = hasAction(Intent.ACTION_OPEN_DOCUMENT_TREE)

        intending(intentMatcher).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, intentResult)
        )
        composeRule.waitUntilExactlyOneExists(hasText("Pilih Lokasi Backup"))
        composeRule.onNodeWithText("Pilih Lokasi Backup").performClick()

        intended(intentMatcher)

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
        composeRule.waitUntilDoesNotExist(hasTestTag(BackupRestoreNodeTag.buatBackupBaruDialog) , timeoutMillis = 5000)
        composeRule.waitUntilExactlyOneExists(hasText(namaBackup))

        return this
    }
}