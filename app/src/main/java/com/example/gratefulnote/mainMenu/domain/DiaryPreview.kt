package com.example.gratefulnote.mainMenu.domain

data class DiaryPreview(
    val id: Long,
    val type: String,
    val title: String,
    val description: String,
    val createdAt: Long,
    val isFavorite: Boolean,
)
