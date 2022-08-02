package com.example.gratefulnote.editpositiveemotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao

class EditPositiveEmotionViewModelFactory(private val dataSource : PositiveEmotionDatabaseDao)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPositiveEmotionViewModel::class.java))
            return EditPositiveEmotionViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}