package com.example.gratefulnote.daily_notification.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gratefulnote.database.DailyNotification

@Composable
fun DailyNotificationCard(
    data : DailyNotification,
    modifier : Modifier = Modifier,
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 24.dp , vertical = 16.dp)
    ) {
        Text(
            text = "${data.hour.toString().padStart(2 , '0')}:" +
                    data.minute.toString().padStart(2 , '0'),
            fontSize = 32.sp,
            color = if (data.isEnabled) Color.Black else Color(0xFFA6A6A6)
        )

        Switch(
            checked = data.isEnabled,
            onCheckedChange = {

            }
        )
    }
}

@Preview
@Composable
fun PreviewEnabledDailyNotificationCard(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DailyNotificationCard(data = DailyNotification(
            hour = 8,
            minute = 1,
            isEnabled = true,
        ) , modifier = Modifier.padding(24.dp))
    }
}

@Preview
@Composable
fun PreviewDisabledDailyNotificationCard(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DailyNotificationCard(data = DailyNotification(
            hour = 23,
            minute = 59,
            isEnabled = false,
        ) , modifier = Modifier.padding(24.dp))
    }
}
