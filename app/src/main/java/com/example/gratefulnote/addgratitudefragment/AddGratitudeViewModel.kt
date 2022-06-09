package com.example.gratefulnote.addgratitudefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddGratitudeViewModel(private val database : PositiveEmotionDatabaseDao) : ViewModel() {
    val typeOfPositiveEmotion = arrayOf(
        "Joy", "Gratitude", "Serenity", "Interest", "Hope",
        "Pride", "Amusement", "Inspiration", "Awe", "Love", "Other"
    )
    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insert(newData : PositiveEmotion){
        uiScope.launch {
            database.insert(newData)
            _backToMain.value = true
        }
    }

    private val _backToMain = MutableLiveData(false)
    val backToMain : LiveData<Boolean>
        get() = _backToMain

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

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