package com.example.gratefulnote.backuprestore.presentation.main_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.presentation.confirm_restore_dialog.ConfirmRestoreDialogSetup
import com.example.gratefulnote.backuprestore.presentation.main_screen.component.BottomActionCard
import com.example.gratefulnote.backuprestore.presentation.main_screen.component.FileList
import com.example.gratefulnote.backuprestore.presentation.new_backup_dialog.NewBackupDialogSetup
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.common.presentation.ResponseWrapperLoader

@Composable
fun BackupRestoreFragmentBodySetup(
    viewModel : MainScreenViewModel = viewModel(),
    notifyRestoreSucceedToMainScreen : () -> Unit,
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
        openDocumentTree = { getDocumentTreeAction.launch(null) },
        notifyRestoreSucceedToMainScreen = notifyRestoreSucceedToMainScreen,
    )
}
@Composable
private fun BackupRestoreFragmentBody(
    state : BackupRestoreViewState,
    onEvent : (BackupRestoreStateEvent) -> Unit,
    openDocumentTree : () -> Unit,
    notifyRestoreSucceedToMainScreen: () -> Unit,
){
    ResponseWrapperLoader(
        response = state.pathLocation,
        onRetry = {
            onEvent(BackupRestoreStateEvent.LoadPathFromSharedPref)
        },
        content = {pathLocation ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (pathLocation == null)
                    ElevatedButton(onClick = {
                        openDocumentTree()
                    }) {
                        Text(text = "Pilih Lokasi Backup")
                    }


                else {
                    FileList(
                        state = state,
                        onEvent = onEvent,
                        modifier = Modifier.weight(1f)
                    )

                    BottomActionCard(
                        currentLocation = pathLocation.path!!,
                        onEvent = onEvent,
                        openDocumentTree = openDocumentTree,
                    )
                }
            }
            if (state.openCreateNewBackupDialog)
                NewBackupDialogSetup(
                    onDismissRequest = { backupStatus ->
                        onEvent(BackupRestoreStateEvent.RequestDismissNewBackupDialog(backupStatus))
                    },
                    documentTreeUri = pathLocation!!
                )
            if (state.restoreFile != null)
                ConfirmRestoreDialogSetup(
                    onDismissRequest = { restoreResponse ->
                        onEvent(
                            BackupRestoreStateEvent.RequestDismissRestoreConfirmationDialog(
                                 restoreResponse
                            )
                        )
                        if (restoreResponse?.isSucceed() == true)
                            notifyRestoreSucceedToMainScreen()
                    },
                    restoreFile = state.restoreFile
                )
        }
    )

}

@Preview(name = "Main body URL Choosen")
@Composable
private fun PreviewLoading(){
    Surface {
        BackupRestoreFragmentBody(
            state = BackupRestoreViewState(
                pathLocation = ResponseWrapper.Succeed(
                    Uri.parse("http://www.google.com.v.abc.pens.ac.id.google.com")
                )
            ),
            onEvent = {},
            openDocumentTree = {},
            notifyRestoreSucceedToMainScreen = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWithListFile(){
    Surface {
        BackupRestoreFragmentBody(
            state = BackupRestoreViewState(
                pathLocation = ResponseWrapper.Succeed(
                    Uri.parse("Download/Backups"),
                ),
                backupFiles = ResponseWrapper.Succeed(
                    data = List(100){ index ->
                        DocumentFileDto(
                            file = DocumentFile.fromSingleUri(
                                LocalContext.current,
                                Uri.parse("content://mock/mockfile.gn_backup.json")
                            )!!,
                            fileName = "File $index",
                            lastModified = "01-Januari-2023"
                        )
                    }
                )
            ),
            onEvent = {},
            openDocumentTree = {},
            notifyRestoreSucceedToMainScreen = {},
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
            openDocumentTree = {},
            notifyRestoreSucceedToMainScreen = {},
        )
    }
}