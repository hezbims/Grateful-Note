package com.example.gratefulnote.backuprestore.presentation.main_screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.gratefulnote.backuprestore.presentation.main_screen.BackupRestoreStateEvent
import com.example.gratefulnote.backuprestore.presentation.main_screen.BackupRestoreViewState
import com.example.gratefulnote.backuprestore.presentation.test_tag.BackupRestoreNodeTag
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.common.presentation.ResponseWrapperLoader

@Composable
fun FileList(
    state : BackupRestoreViewState,
    onEvent : (BackupRestoreStateEvent) -> Unit,
    modifier: Modifier = Modifier
){
    ResponseWrapperLoader(
        response = state.backupFiles ?: ResponseWrapper.Loading(),
        onRetry = {
            onEvent(BackupRestoreStateEvent.ReloadBackupFileList)
        },
        modifier = modifier,
        content = { documentFiles ->
            if (documentFiles.isNullOrEmpty()) {
                Text(text = "Tidak ada data")
                return@ResponseWrapperLoader
            }

            LazyColumn(
                contentPadding = PaddingValues(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(BackupRestoreNodeTag.backupFilesLazyColumn)
            ) {
                items(documentFiles){
                    FileListItem(
                        file = it,
                        onDeleteFile = { file ->
                            onEvent(BackupRestoreStateEvent.DeleteFile(file))
                        },
                        onRestoreFile = { file ->
                            onEvent(
                                BackupRestoreStateEvent.OpenRestoreConfirmationDialog(file)
                            )
                        }
                    )
                }
            }
        }
    )
}