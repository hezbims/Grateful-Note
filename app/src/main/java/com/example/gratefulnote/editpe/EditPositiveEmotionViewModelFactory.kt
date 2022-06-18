package com.example.gratefulnote.editpe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import com.example.gratefulnote.mainfragment.MainViewModel
import java.lang.IllegalArgumentException

class EditPositiveEmotionViewModelFactory(private val dataSource : PositiveEmotionDatabaseDao ,
                                          private val id : Long
                                          ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPositiveEmotionViewModel::class.java))
            return EditPositiveEmotionViewModel(dataSource , id) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}