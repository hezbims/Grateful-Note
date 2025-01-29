package com.example.gratefulnote.common.diary.domain.model

/**
 * Class dimana user bisa mengubah-ubah data diary dengan textfield
 */
data class DiaryUserInput(
    val title: String,
    val description: String,
    val tag: String,
    val id: Long
)