package com.example.gratefulnote.daily_notification.domain.service

interface IDailyAlarmSetter {
    fun enableDailyAlarm(hour: Int, minute: Int, id: Int, forceToNextDay : Boolean = false)
    fun disableDailyAlarm(id: Int)
    fun canScheduleExactAlarm() : Boolean
}