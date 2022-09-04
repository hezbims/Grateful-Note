package com.example.gratefulnote.editpositiveemotion

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditPositiveEmotionViewModelFactory(
    private val app : Application,
    private val id : Long
    )
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPositiveEmotionViewModel::class.java))
            return EditPositiveEmotionViewModel(app , id) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}