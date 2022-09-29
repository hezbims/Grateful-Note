package com.example.gratefulnote.mainfragment

import android.content.Context
import com.example.gratefulnote.R

class FilterState(private val context : Context){
    private val months: Array<String> = context.resources.getStringArray(R.array.months_list)

    private var _selectedMonth = 0
    val selectedMonth : Int
        get() = _selectedMonth
    val stringSelectedMonth : String
        get() = months[_selectedMonth]

    private var _selectedYear : Int? = null
    val selectedYear : Int?
        get() = _selectedYear
    val stringSelectedYear : String
        get() = _selectedYear?.toString() ?: getString(R.string.semua)

    private var _selectedPositiveEmotion = getString(R.string.semua)
    val selectedPositiveEmotion : String
        get() = _selectedPositiveEmotion

    private var _switchState = true
    val switchState : Boolean
        get() = _switchState

    private var _onlyFavorite = false
    val onlyFavorite : Boolean
        get() = _onlyFavorite

    fun getString(id : Int) =
        context.getString(id)

    companion object{
        fun getInstance(newSelectedMonth: String ,
                        newSelectedYear : String ,
                        newSelectedPositiveEmotion : String ,
                        newSwitchState : Boolean,
                        newOnlyFavorite : Boolean,
                        context: Context
        ) =
            FilterState(context).apply {
                _selectedMonth = months.indexOf(newSelectedMonth)
                _selectedYear =
                    if (newSelectedYear == getString(R.string.semua)) null
                    else newSelectedYear.toInt()
                _selectedPositiveEmotion = newSelectedPositiveEmotion
                _switchState = newSwitchState
                _onlyFavorite = newOnlyFavorite
            }
    }
}