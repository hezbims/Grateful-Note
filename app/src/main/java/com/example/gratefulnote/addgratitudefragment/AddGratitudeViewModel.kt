package com.example.gratefulnote.addgratitudefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.*

class AddGratitudeViewModel(private val database : PositiveEmotionDatabaseDao) : ViewModel() {
    val typeOfPositiveEmotion = arrayOf(
        "Joy", "Gratitude", "Serenity", "Interest", "Hope",
        "Pride", "Amusement", "Inspiration", "Awe", "Love", "Other"
    )
    fun insert(newData : PositiveEmotion){
        viewModelScope.launch(Dispatchers.IO) {
            database.insert(newData)
            withContext(Dispatchers.Main) {
                _backToMain.value = true
            }
        }
    }

    private val _backToMain = MutableLiveData(false)
    val backToMain : LiveData<Boolean>
        get() = _backToMain

    private val _navigateToHelp = MutableLiveData(false)
    val navigateToHelp : LiveData<Boolean>
        get() = _navigateToHelp
    fun onClickGetHelp(){
        _navigateToHelp.value = true
    }
    fun doneNavigatingToHelp(){
        _navigateToHelp.value = false
    }
}