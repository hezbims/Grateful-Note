package com.example.gratefulnote.backuprestore.presentation.main_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import com.example.gratefulnote.backuprestore.presentation.new_backup_dialog.NewBackupDialogSetup
import com.example.gratefulnote.backuprestore.presentation.main_screen.component.FileListItem
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.presentation.confirm_restore_dialog.ConfirmRestoreDialogSetup
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.common.presentation.ResponseWrapperLoader

@Composable
fun BackupRestoreFragmentBodySetup(
    viewModel : BackupRestoreViewModel
){
    val getDocumentTreeAction = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ){
        if (it != null){
            viewModel.onEvent(BackupRestoreStateEvent.UpdatePathLocation(it))
        }
    }

    BackupRestoreFragmentBody(
        state = viewModel.backupRestoreState.collectAsState().value,
        onEvent = viewModel::onEvent,
        openDocumentTree = { getDocumentTreeAction.launch(null) }
    )
}
@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun BackupRestoreFragmentBody(
    state : BackupRestoreViewState,
    onEvent : (BackupRestoreStateEvent) -> Unit,
    openDocumentTree : () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.pathLocation == null)
            ElevatedButton(onClick = {
                openDocumentTree()
            }) {
                Text(text = "Pilih Lokasi Backup")
            }


        else {
            ResponseWrapperLoader(
                response = state.backupFiles ?: ResponseWrapper.ResponseLoading(),
                onRetry = {
                    onEvent(BackupRestoreStateEvent.ReloadBackupFileList)
                },
                modifier = Modifier
                    .weight(1f),
                content = { documentFiles ->
                    if (documentFiles.isNullOrEmpty()) {
                        Text(text = "Tidak ada data")
                        return@ResponseWrapperLoader
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxSize()
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

            Card (
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()


            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = 16.dp,
                            bottom = 12.dp
                        )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        Button(
                            onClick = {
                                openDocumentTree()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Ubah Lokasi")
                        }
                        Button(
                            onClick = {
                                onEvent(BackupRestoreStateEvent.OpenNewBackupDialog)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Buat Backup Baru")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        fontWeight = FontWeight.Bold,
                        text = "Lokasi sekarang : ${state.pathLocation.path}",
                        modifier = Modifier.basicMarquee(),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
    if (state.openCreateNewBackupDialog)
        NewBackupDialogSetup(
            onDismissRequest = { backupStatus ->
                onEvent(BackupRestoreStateEvent.RequestDismissNewBackupDialog(backupStatus))
            },
            documentTreeUri = state.pathLocation!!
        )
    if (state.restoreFile != null)
        ConfirmRestoreDialogSetup(
            onDismissRequest = {backupStatus ->
                onEvent(
                    BackupRestoreStateEvent.RequestDismissRestoreConfirmationDialog(
                        backupStatus
                    )
                )
            },
            restoreFile = state.restoreFile
        )
}

@Preview(name = "Main body URL Choosen")
@Composable
private fun PreviewLoading(){
    Surface {
        BackupRestoreFragmentBody(
            state = BackupRestoreViewState(
                pathLocation = Uri.parse("http://www.google.com.v.abc.pens.ac.id.google.com")
            ),
            onEvent = {},
            openDocumentTree = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWithListFile(){
    Surface {
        BackupRestoreFragmentBody(
            state = BackupRestoreViewState(
                pathLocation = Uri.parse("Download/Backups"),
                backupFiles = ResponseWrapper.ResponseSucceed(
                    data = List(100){ _ ->
                        DocumentFileDto.from(
                            DocumentFile.fromSingleUri(
                                LocalContext.current,
                                Uri.parse("content://mock/mockfile.gn_backup.json")
                            )!!
                        )
                    }
                )
            ),
            onEvent = {},
            openDocumentTree = {}
        )
    }
}

@Preview
@Composable
private fun PreviewPathLocationNull(){
    Surface {
        BackupRestoreFragmentBody(
            state = BackupRestoreViewState(),
            onEvent = {},
            openDocumentTree = {}
        )
    }
}