package com.example.gratefulnote.backuprestore.presentation.main_screen.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.documentfile.provider.DocumentFile
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.presentation.test_tag.BackupRestoreNodeTag

@Composable
fun FileListItem(
    file : DocumentFileDto,
    onDeleteFile : (DocumentFileDto) -> Unit,
    onRestoreFile : (DocumentFileDto) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.testTag(BackupRestoreNodeTag.fileListItem)
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
                    text = file.fileName
                )

                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Hapus file backup",
                    modifier = Modifier.clickable {
                        onDeleteFile(file)
                    }
                )
            }

            Text(
                fontSize = 12.sp,
                text = "Terkahir diubah : ${file.lastModified}"
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontSize = 12.sp,
                    text = "Size : ${String.format("%.1f Mb", file.file.length() / 1000000f)}"
                )

                ElevatedButton(onClick = {
                    onRestoreFile(file)
                }) {
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
    val mockFile = DocumentFileDto.from(
        DocumentFile.fromSingleUri(
            LocalContext.current, Uri.parse("content://mock/mockfile.gn_backup.json")
        )!!
    )

    Surface {
        Surface(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            FileListItem(
                file = mockFile,
                onDeleteFile = {_ ->},
                onRestoreFile = {_ ->},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
