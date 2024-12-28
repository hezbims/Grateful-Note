package com.example.gratefulnote.addNewDiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.database.Diary
import com.example.gratefulnote.database.DiaryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddNewDiaryViewModel
    @Inject constructor(private val dao : DiaryDao) : ViewModel() {
    fun insert(newData : Diary){
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(newData)
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