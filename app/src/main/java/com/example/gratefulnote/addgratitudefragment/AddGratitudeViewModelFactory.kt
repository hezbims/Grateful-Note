package com.example.gratefulnote.addgratitudefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import java.lang.IllegalArgumentException

class AddGratitudeViewModelFactory(private val dataSource : PositiveEmotionDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGratetitudeViewModel::class.java))
            return AddGratetitudeViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}