package com.example.gratefulnote.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val dataSource : PositiveEmotionDatabaseDao) : ViewModel(){
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /* Menghapus item kalau tong sampah dipencet */
    fun delete(id : Long){
        viewModelScope.launch {
            dataSource.delete(id)
        }
    }


    /* Semua recyclerView Data */
    val recyclerViewData = dataSource.getAllPositiveEmotion()


    /* Mengatur perpindahan fragment ke AddGratitude */
    private val _eventMoveToAddGratitude = MutableLiveData(false)
    val eventMoveToAddGratitude : LiveData<Boolean>
        get() = _eventMoveToAddGratitude

    fun onClickAddNewGratitude(){
        _eventMoveToAddGratitude.value = true
        viewModelJob.cancel()
    }

    fun doneNavigating(){
        _eventMoveToAddGratitude.value = false
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }


    /* Mengatur Selected Date */
    private val _selectedDateString = MutableLiveData<String>("--/-/----")
    val selectedDateString : LiveData<String>
        get() = _selectedDateString

    fun setDateString(date : String) {
        _selectedDateString.value = date
    }

}