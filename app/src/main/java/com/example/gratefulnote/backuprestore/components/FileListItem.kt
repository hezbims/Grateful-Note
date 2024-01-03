package com.example.gratefulnote.backuprestore.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import java.text.SimpleDateFormat

@Composable
fun FileListItem(
    file : DocumentFile,
    onDeleteFile : (DocumentFile) -> Unit,
    onRestoreFile : (DocumentFile) -> Unit,
    modifier: Modifier = Modifier
){
    val dateFormatter = remember {
        SimpleDateFormat("dd-LLL-yyyy, HH:mm")
    }

    Column(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontSize = 15.sp,
                    text = file.name ?: "*This file has no name*"
                )

                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Hapus file backup",
                    modifier = Modifier.clickable { }
                )
            }

            Text(
                fontSize = 12.sp,
                text = "Terkahir diubah : ${
                    dateFormatter.format(file.lastModified())
                }"
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontSize = 12.sp,
                    text = "Size : ${String.format("%.1f Mb", file.length() / 1000000f)}"
                )

                ElevatedButton(onClick = { /*TODO*/ }) {
                    Text("Restore")
                }
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
        )
    }
}


@Preview
@Composable
private fun PreviewFileListItem(){
    val mockFile = DocumentFile.fromSingleUri(
        LocalContext.current, Uri.parse("content://mock/mockfile.gn_backup.json")
    )

    Surface {
        Surface(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            FileListItem(
                file = mockFile!!,
                onDeleteFile = {_ ->},
                onRestoreFile = {_ ->},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
