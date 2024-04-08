package com.example.gratefulnote.daily_notification.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gratefulnote.database.DailyNotificationEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailyNotificationCard(
    dailyNotificationUiModel : DailyNotificationUiModel,
    onLongClick : () -> Unit,
    modifier : Modifier = Modifier,
    onClickWhenSelectModeActivated : (() -> Unit)? = null,
){
    val data = dailyNotificationUiModel.data
    Card(
        modifier = Modifier.combinedClickable(
            onClick = onClickWhenSelectModeActivated ?: {},
            onLongClick = onLongClick
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "${data.hour.toString().padStart(2, '0')}:" +
                        data.minute.toString().padStart(2, '0'),
                fontSize = 32.sp,
                color = if (data.isEnabled) Color.Black else Color(0xFFA6A6A6)
            )
            
            if (onClickWhenSelectModeActivated != null)
                Checkbox(
                    checked = dailyNotificationUiModel.isSelectedForDeleteCandidate,
                    onCheckedChange = null,
                )

            else
                Switch(
                    checked = data.isEnabled,
                    onCheckedChange = {

                    },
                    modifier = Modifier.scale(0.8f)
                )
        }
    }
}

@Preview
@Composable
fun PreviewEnabledDailyNotificationCard(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DailyNotificationCard(
            dailyNotificationUiModel = DailyNotificationUiModel(
                data = DailyNotificationEntity(
                hour = 8,
                minute = 1,
                isEnabled = true,
                ),
                isSelectedForDeleteCandidate = false,
            ),
            onLongClick = {},
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview
@Composable
fun PreviewDisabledDailyNotificationCard(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DailyNotificationCard(
            dailyNotificationUiModel = DailyNotificationUiModel(
                data = DailyNotificationEntity(
                    hour = 23,
                    minute = 59,
                    isEnabled = false,
                ),
                isSelectedForDeleteCandidate = false,
            ),
            onLongClick = {},
            modifier = Modifier.padding(24.dp))
    }
}
