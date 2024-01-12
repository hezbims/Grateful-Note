package com.example.gratefulnote.backuprestore.presentation.main_screen.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gratefulnote.backuprestore.presentation.main_screen.BackupRestoreStateEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomActionCard(
    currentLocation : String,
    onEvent : (BackupRestoreStateEvent) -> Unit,
    openDocumentTree : () -> Unit,
    modifier: Modifier = Modifier
){
    Card (
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
        ),
        modifier = modifier
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
                text = "Lokasi sekarang : $currentLocation",
                modifier = Modifier.basicMarquee(),
                fontSize = 15.sp
            )
        }
    }
}