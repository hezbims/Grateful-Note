package com.example.gratefulnote.notification

import android.content.Context
import android.content.SharedPreferences
import com.example.gratefulnote.R
import java.util.*

class Clock(private val hours : Int, private val minutes : Int){
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
        fun getSavedClock(sharedPreferences: SharedPreferences, context: Context) =
            Clock(
                sharedPreferences.getInt(context.getString(R.string.saved_hours) , 0),
                sharedPreferences.getInt(context.getString(R.string.saved_minutes) , 0)
            )
    }
}