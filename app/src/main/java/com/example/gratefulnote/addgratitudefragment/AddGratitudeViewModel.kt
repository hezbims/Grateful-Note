package com.example.gratefulnote.addgratitudefragment

import androidx.lifecycle.*
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDao
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class AddGratitudeViewModel(private val database : PositiveEmotionDao) : ViewModel() {
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

class AddGratitudeViewModelFactory(private val dataSource : PositiveEmotionDao) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddGratitudeViewModel::class.java))
            return AddGratitudeViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}