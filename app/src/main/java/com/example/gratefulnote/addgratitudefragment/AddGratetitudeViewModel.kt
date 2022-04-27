package com.example.gratefulnote.addgratitudefragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.*

class AddGratetitudeViewModel(private val database : PositiveEmotionDatabaseDao) : ViewModel() {
    companion object {
        val typeOfPositiveEmotion = arrayOf(
            "Joy", "Gratitude", "Serenity", "Interest", "Hope",
            "Pride", "Amusement", "Inspiration", "Love"
        )
    }

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insert(newData : PositiveEmotion){
        uiScope.launch {
            database.insert(newData)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}