package com.example.gratefulnote.notification

import android.content.Context
import android.os.Parcelable
import com.example.gratefulnote.R
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Clock(val hours : Int, val minutes : Int) : Parcelable{
    fun format() =
        "${timeFormat(hours)}:${timeFormat(minutes)}"

    private fun timeFormat(time : Int) =
        if (time < 10) "0$time" else time.toString()

    fun timeInMillis() =
        Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY , hours)
            set(Calendar.MINUTE , minutes)
            set(Calendar.SECOND , 0)
        }.timeInMillis

    companion object{
        fun getSavedClock(context: Context) : Clock{
            val sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_name),
                Context.MODE_PRIVATE
            )

            val timeInMillis = sharedPreferences.getLong(
                context.getString(R.string.saved_time_in_millis),
                Clock(0 , 0).timeInMillis()
            )

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis

            return Clock(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        }
    }
}