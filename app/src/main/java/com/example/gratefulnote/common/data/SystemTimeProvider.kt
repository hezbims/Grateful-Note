package com.example.gratefulnote.common.data

import com.example.gratefulnote.common.domain.ITimeProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class SystemTimeProvider @Inject constructor(): ITimeProvider {
    override fun getCurrentCalendar(): Calendar {
        return Calendar.getInstance()
    }

    override fun formatCurrentCalendar(pattern: String): String {
        return SimpleDateFormat(
            pattern,
            Locale.getDefault()
        ).format(getCurrentCalendar().time)
    }

    override fun getCurrentTimezone(): TimeZone {
        return TimeZone.getDefault()
    }

    override fun getCurrentTimeInMillis(): Long {
        return getCurrentCalendar().timeInMillis
    }
}