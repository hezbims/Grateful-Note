package com.example.gratefulnote.common.data

import com.example.gratefulnote.common.domain.ITimeProvider
import java.util.Calendar
import javax.inject.Inject

class SystemTimeProvider @Inject constructor(): ITimeProvider {
    override fun getCurrentCalendar(): Calendar {
        return Calendar.getInstance()
    }

    override fun getCurrentTimeInMillis(): Long {
        return getCurrentCalendar().timeInMillis
    }
}