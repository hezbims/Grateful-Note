package com.example.gratefulnote.common.diary.domain.model

data class FilterState (
    val month : Int? = null,
    val year : Int? = null,
    val type : String? = null,
    val isSortedLatest : Boolean = true,
    val isOnlyFavorite : Boolean = false,
)