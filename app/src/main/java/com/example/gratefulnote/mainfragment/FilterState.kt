package com.example.gratefulnote.mainfragment

data class FilterState (
    val selectedMonth : Int? = null,
    val selectedYear : Int? = null,
    val positiveEmotionType : String? = null,
    val isSortedLatest : Boolean = true,
    val isOnlyFavorite : Boolean = true,
)