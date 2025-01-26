package com.example.gratefulnote.common.diary.domain.model

data class DiaryDetails(
    val id : Long,
    val title: String,
    val description: String,
    val type: String,

    var day : Int,
    var month : Int,
    var year : Int,

    val isFavorite : Boolean,

    val createdAt : Long,

    val updatedAt : Long,
)