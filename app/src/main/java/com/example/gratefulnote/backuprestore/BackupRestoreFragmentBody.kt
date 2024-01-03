package com.example.gratefulnote.backuprestore

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import com.example.gratefulnote.backuprestore.components.FileListItem
import com.example.gratefulnote.common.data.ResponseWrapper
import com.example.gratefulnote.common.presentation.ResponseWrapperLoader

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun BackupRestoreFragmentBody(
    state : BackupRestoreViewState,
    onEvent : (BackupRestoreStateEvent) -> Unit,
    launchActivityResultEvent : (BackupRestoreActivityEvent) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (state.pathLocation == null)
            ElevatedButton(onClick = {
                launchActivityResultEvent(
                    BackupRestoreActivityEvent.OpenDocumentTree
                )
            }) {
                Text(text = "Pilih Lokasi Backup")
            }

        else {
            ResponseWrapperLoader<List<DocumentFile>>(
                response = state.backupFiles ?: ResponseWrapper.ResponseLoading(),
                onRetry = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f),
                content = { documentFiles ->
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(documentFiles!!){
                            FileListItem(
                                file = it,
                                onDeleteFile = { file ->

                                },
                                onRestoreFile = { file ->

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
                                launchActivityResultEvent(
                                    BackupRestoreActivityEvent.OpenDocumentTree
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Ubah Lokasi")
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Buat Backup Baru")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        fontWeight = FontWeight.Bold,
                        text = "Lokasi sekarang : ${state.pathLocation}",
                        modifier = Modifier.basicMarquee(),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Preview(name = "Main body URL Choosen")
@Composable
private fun PreviewLoading(){
    Surface {
        BackupRestoreFragmentBody(
            state = BackupRestoreViewState(
                pathLocation = Uri.parse("http://www.google.com.v.abc.pens.ac.id.google.com")
            ),
            onEvent = {_ -> },
            launchActivityResultEvent = { _ -> }
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
                backupFiles = ResponseWrapper.ResponseLoaded<List<DocumentFile>>(
                    data = List(100){index ->
                        DocumentFile.fromSingleUri(
                            LocalContext.current,
                            Uri.parse("content://mock/mockfile.gn_backup.json")
                        )!!
                    }
                )
            ),
            onEvent = { _ -> },
            launchActivityResultEvent = { _ -> }
        )
    }
}