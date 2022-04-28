package com.example.gratefulnote.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao

class MainViewModel(private val dataSource : PositiveEmotionDatabaseDao) : ViewModel(){
    val recyclerViewData = dataSource.getAllPositiveEmotion()

    private val _eventMoveToAddGratitude = MutableLiveData<Boolean>(false)
    val eventMoveToAddGratitude : LiveData<Boolean>
        get() = _eventMoveToAddGratitude

    fun onClickAddNewGratitude(){
        _eventMoveToAddGratitude.value = true
    }

    fun doneNavigating(){
        _eventMoveToAddGratitude.value = false
    }
}