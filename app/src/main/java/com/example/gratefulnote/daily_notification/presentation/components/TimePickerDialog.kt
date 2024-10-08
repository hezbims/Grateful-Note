package com.example.gratefulnote.daily_notification.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gratefulnote.R
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.daily_notification.presentation.DailyNotificationEvent
import com.example.gratefulnote.daily_notification.presentation.DailyNotificationState
import com.example.gratefulnote.daily_notification.test_tag.DailyNotificationTestTag

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ComposeTimePickerDialog(
    state : DailyNotificationState,
    onEvent: (DailyNotificationEvent) -> Unit,
){
    val timePickerState = rememberTimePickerState()
    Dialog(onDismissRequest = {
        onEvent(DailyNotificationEvent.OnDismissTimePickerDialog)
    }) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                TimePicker(
                    state = timePickerState,
                    modifier = Modifier.testTag(DailyNotificationTestTag.timePicker)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(onClick = {
                        onEvent(DailyNotificationEvent.OnDismissTimePickerDialog)
                    }) {
                        Text(text = "Batal")
                    }

                    if (state.createNewDailyNotificationStatus is ResponseWrapper.Loading)
                        CircularProgressIndicator()
                    else
                        TextButton(onClick = {
                            onEvent(
                                DailyNotificationEvent.OnCreateNewDailyNotification(
                                    hour = timePickerState.hour, minute = timePickerState.minute
                                )
                            )
                        }) {
                            Text(text = stringResource(R.string.konfirmasi))
                        }
                }
            }
        }
    }

}