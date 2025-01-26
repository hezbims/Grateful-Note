package com.example.gratefulnote.common.domain

import java.util.Calendar

interface ITimeProvider {
    fun getCurrentCalendar(): Calendar
    fun getCurrentTimeInMillis(): Long
}