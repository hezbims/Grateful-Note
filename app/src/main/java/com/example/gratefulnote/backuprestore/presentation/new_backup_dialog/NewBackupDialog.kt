package com.example.gratefulnote.backuprestore.presentation.new_backup_dialog

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gratefulnote.backuprestore.presentation.test_tag.BackupRestoreNodeTag
import com.example.gratefulnote.common.data.dto.ResponseWrapper

@Composable
fun NewBackupDialogSetup(
    onDismissRequest: (ResponseWrapper<Nothing>?) -> Unit,
    documentTreeUri: Uri,
    viewModel: NewBackupDialogViewModel = viewModel(),
){
    DisposableEffect(Unit){
        viewModel.onEvent(
            CreateNewBackupDialogEvent.OnInitDocumentTreeUri(documentTreeUri)
        )

        onDispose {
            viewModel.
                onEvent(
                    CreateNewBackupDialogEvent.OnResetViewModelState
                )
        }
    }

    val state = viewModel.state.collectAsState().value
    val currentContext = LocalContext.current

    LaunchedEffect(state.createNewBackupStatus){
        if (state.createNewBackupStatus is ResponseWrapper.ResponseSucceed<*>) {
            Toast.makeText(
                currentContext,
                "Berhasil membuat backup baru!" ,
                Toast.LENGTH_SHORT
            ).show()
            onDismissRequest(state.createNewBackupStatus)
        }
    }

    NewBackupDialog(
        onDismissRequest = onDismissRequest,
        state = state,
        onEvent = viewModel::onEvent
    )
}
@Composable
private fun NewBackupDialog(
    onDismissRequest : (ResponseWrapper<Nothing>?) -> Unit,
    state : CreateNewBackupDialogState,
    onEvent : (CreateNewBackupDialogEvent) -> Unit,
){
    Dialog(onDismissRequest = {
        onDismissRequest(state.createNewBackupStatus)
    }) {
        Card(modifier = Modifier.testTag(BackupRestoreNodeTag.buatBackupBaruDialog)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .widthIn(0.dp, 312.dp)
                    .padding(24.dp)
            ) {
                Text(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    text = "Buat Backup Baru"
                )

                OutlinedTextField(
                    label = {
                        Text(text = "Judul Backup")
                    },
                    value = state.backupTitle,
                    onValueChange = { newValue ->
                        onEvent(CreateNewBackupDialogEvent.OnChangeBackupTitle(newValue))
                    },
                    isError = state.createNewBackupStatus is ResponseWrapper.ResponseError,
                    supportingText = {
                        if (state.createNewBackupStatus is ResponseWrapper.ResponseError)
                            Text(
                                text = state.createNewBackupStatus.exception?.message ?:
                                    "Unknown Error occured",
                                color = MaterialTheme.colorScheme.error,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )

                    },
                    modifier = Modifier.testTag(BackupRestoreNodeTag.buatBackupBaruTextField)
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        ),
                        onClick = {
                            onDismissRequest(state.createNewBackupStatus)
                        }
                    ) {
                        Text("Batal")
                    }

                    Spacer(modifier = Modifier.width(24.dp))


                    if (state.createNewBackupStatus !is ResponseWrapper.ResponseLoading)
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Blue
                            ),
                            onClick = {
                                onEvent(CreateNewBackupDialogEvent.OnCreateNewBackup)
                            }
                        ) {
                            Text("Buat")
                        }
                    else
                        CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDialog(){
    Surface {
        NewBackupDialog(
            onDismissRequest = { /*TODO*/ },
            state = CreateNewBackupDialogState(),
            onEvent = {_ -> }
        )
    }
}