package com.example.gratefulnote.hilt_module

import com.example.gratefulnote.common._hilt_module.TimeProviderModule
import com.example.gratefulnote.common.domain.ITimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TimeProviderModule::class]
)
object MockTimeProviderModule {
    @Provides
    fun provideTestTimeProvider() : ITimeProvider =
        object : ITimeProvider {
            override fun getCurrentCalendar(): Calendar =
                Calendar.getInstance().apply {
                    timeZone = getCurrentTimezone()
                    set(Calendar.YEAR, 2020)
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_MONTH, 15)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

            override fun getCurrentTimezone(): TimeZone {
                return TimeZone.getTimeZone("UTC")
            }

            override fun formatCurrentCalendar(pattern: String): String {
                return SimpleDateFormat(pattern).format(getCurrentCalendar().time)
            }

            override fun getCurrentTimeInMillis(): Long =
                getCurrentCalendar().timeInMillis
        }
}