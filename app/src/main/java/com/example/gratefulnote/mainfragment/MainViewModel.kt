package com.example.gratefulnote.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
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