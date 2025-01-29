package com.example.gratefulnote.common.domain

import java.util.Calendar
import java.util.TimeZone

interface ITimeProvider {
    fun getCurrentCalendar(): Calendar
    fun formatCurrentCalendar(pattern: String): String
    fun getCurrentTimezone() : TimeZone
    fun getCurrentTimeInMillis(): Long
}