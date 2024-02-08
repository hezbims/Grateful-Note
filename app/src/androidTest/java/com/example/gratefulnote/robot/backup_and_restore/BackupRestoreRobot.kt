package com.example.gratefulnote.robot.backup_and_restore

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.robot._common.BaseRobot

@OptIn(ExperimentalTestApi::class)
class BackupRestoreRobot(
    private val composeRule : AndroidComposeTestRule<ActivityScenarioRule<MainActivity> , MainActivity>
) : BaseRobot() {
    fun clickPilihLokasiBackup(){
//        val cacheDirUri = InstrumentationRegistry
//            .getInstrumentation()
//            .targetContext
//            .externalCacheDir!!
//
//
//        val documentFile = DocumentFile.fromFile(cacheDirUri)
//
//        val documentTreeUri = DocumentsContract.buildTreeDocumentUri(
//            cacheDirUri.authority ,
//            DocumentsContract.getDocumentId(cacheDirUri)
//        )
//
//        val intentResult = Intent().apply {
//            data = documentTreeUri
//        }
//        val intentMatcher = hasAction(Intent.ACTION_OPEN_DOCUMENT_TREE)
//
//        intending(intentMatcher).respondWith(
//           ActivityResult(Activity.RESULT_OK , intentResult)
//        )
//        composeRule.waitUntilExactlyOneExists(hasText("Pilih Lokasi Backup"))
//        composeRule.onNodeWithText("Pilih Lokasi Backup").performClick()
//
//        intended(intentMatcher)
//
//
//        composeRule.waitUntilExactlyOneExists(hasTestTag(BackupRestoreTag.bottomActionCard))
    }
}