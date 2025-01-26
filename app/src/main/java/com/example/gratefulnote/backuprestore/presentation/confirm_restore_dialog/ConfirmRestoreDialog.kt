package com.example.gratefulnote.backuprestore.presentation.confirm_restore_dialog

import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.presentation.test_tag.BackupRestoreNodeTag
import com.example.gratefulnote.common.domain.ResponseWrapper


@Composable
fun ConfirmRestoreDialogSetup(
    onDismissRequest: (ResponseWrapper<Unit>?) -> Unit,
    restoreFile: DocumentFileDto,
    viewModel: ConfirmRestoreDialogViewModel = viewModel()
){
    viewModel.onEvent(ConfirmRestoreDialogEvent.InitState(restoreFile))
    DisposableEffect(Unit){
        onDispose {
            viewModel.onEvent(ConfirmRestoreDialogEvent.ResetState)
        }
    }

    ConfirmRestoreDialog(
        onDismissRequest = onDismissRequest,
        viewModel = viewModel
    )
}
@Composable
private fun ConfirmRestoreDialog(
    onDismissRequest : (ResponseWrapper<Unit>?) -> Unit,
    viewModel: ConfirmRestoreDialogViewModel
){
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    LaunchedEffect(state.restoreState){
        if (state.restoreState is ResponseWrapper.Succeed){
            Toast.makeText(
                context,
                "Berhasil merestore file!",
                Toast.LENGTH_SHORT
            ).show()
            onDismissRequest(state.restoreState)
        }
        else if (state.restoreState is ResponseWrapper.Error){
            Toast.makeText(
                context,
                "Gagal merestore file : ${
                    state.restoreState.exception?.message ?: "Unknown Error"
                }",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    ConfirmRestoreDialog(
        onDismissRequest = onDismissRequest,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun ConfirmRestoreDialog(
    onDismissRequest: (ResponseWrapper<Unit>?) -> Unit,
    state : ConfirmRestoreDialogState,
    onEvent : (ConfirmRestoreDialogEvent) -> Unit,
){
    AlertDialog(
        icon = {
            Icon(Icons.Default.WarningAmber, contentDescription = "Warning")
        },
        title = {
            Text(
                text = "Apakah anda yakin ingin me-restore file ini ?",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(text = buildAnnotatedString {
                append("Semua note anda yang sekarang akan terhapus dan " +
                        "tergantikan dengan semua note yang ada pada file ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
                    append("'${state.restoreFile!!.fileName}'")
                }
                append(". Apakah anda yakin?")
            }, textAlign = TextAlign.Center)
        },
        onDismissRequest = {
            onDismissRequest(state.restoreState)
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissRequest(state.restoreState)
            }) {
                Text(text = "TIDAK")
            }
        },
        confirmButton = {
            if (state.restoreState is ResponseWrapper.Loading)
                CircularProgressIndicator()
            else
                TextButton(onClick = {
                    onEvent(ConfirmRestoreDialogEvent.RestoreConfirmed)
                }) {
                    Text(text = "YA")
                }
        },
        modifier = Modifier.testTag(BackupRestoreNodeTag.confirmRestoreDialog)
    )

}

@Preview
@Composable
private fun PreviewDialog(){
    Surface {
        ConfirmRestoreDialog(
            onDismissRequest = {_ -> },
            state = ConfirmRestoreDialogState(
                restoreFile = DocumentFileDto(
                    fileName = "Backup terakhir januari",
                    file = DocumentFile.fromSingleUri(
                        LocalContext.current,
                        Uri.parse("content://mock/mockfile.gn_backup.json")
                    )!!,
                    lastModified = "20 Januari 2023"
                )
            ),
            onEvent = {_ -> }
        )
    }
}