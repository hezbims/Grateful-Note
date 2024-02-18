package com.example.gratefulnote.daily_notification.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ComposeTimePickerDialog(
    onEvent: (DailyNotificationEvent) -> Unit,
){
    val state = rememberTimePickerState()
    Dialog(onDismissRequest = {
        onEvent(DailyNotificationEvent.OnDismissTimePickerDialog)
    }) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                TimePicker(state = state)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(onClick = {
                        onEvent(DailyNotificationEvent.OnDismissTimePickerDialog)
                    }) {
                        Text(text = "Batal")
                    }

                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Konfirmasi")
                    }
                }
            }
        }
    }

}