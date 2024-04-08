package com.example.gratefulnote.daily_notification.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gratefulnote.daily_notification.presentation.DailyNotificationEvent
import com.example.gratefulnote.daily_notification.presentation.DailyNotificationState

@Composable
fun ConfirmDeleteDailyNotificationsDialog(
    state : DailyNotificationState,
    onEvent : (DailyNotificationEvent) -> Unit,
){
    val totalItem = remember(state.listDailyNotification) {
        state.listDailyNotification.count {
            it.isSelectedForDeleteCandidate
        }
    }

    AlertDialog(
        icon = {
            Icon(
                Icons.Filled.WarningAmber,
                contentDescription = "Confirm Delete Icon",
                modifier = Modifier.size(72.dp)
            )
        },
        text = {
            Text(
                text = "Apakah anda yakin ingin menghapus $totalItem buah item?",
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        },
        onDismissRequest = {
            onEvent(DailyNotificationEvent.OnDismissDeleteDialog)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(DailyNotificationEvent.OnConfirmDeleteDialog)
                }
            ) {
                Text("YA")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(DailyNotificationEvent.OnDismissDeleteDialog)
                }
            ) {
                Text("TIDAK")
            }
        }
    )
}