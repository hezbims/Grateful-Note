package com.example.gratefulnote.mainfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val dataSource : PositiveEmotionDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}