package com.example.gratefulnote.daily_notification.presentation

import com.example.gratefulnote.database.DailyNotificationEntity

data class DailyNotificationUiModel(
    val data : DailyNotificationEntity,
    val isSelectedForDeleteCandidate: Boolean
)