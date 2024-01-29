package com.example.gratefulnote.notification

import android.content.Context
import android.os.Parcelable
import com.example.gratefulnote.R
import com.example.gratefulnote.common.constants.Constants
import kotlinx.parcelize.Parcelize
import java.util.Calendar

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
            set(Calendar.MILLISECOND , 0)
        }.timeInMillis

    companion object{
        fun getSavedClock(context : Context) : Clock{
            val sharedPreferences = context.getSharedPreferences(
                Constants.SharedPrefs.Notification.name,
                Context.MODE_PRIVATE
            )

            return Clock(
                sharedPreferences.getInt(context.getString(R.string.saved_hours) , 0),
                sharedPreferences.getInt(context.getString(R.string.saved_minutes) , 0)
            )
        }

        fun getNowTimeInMillis() =
            Calendar.getInstance().timeInMillis
    }
}