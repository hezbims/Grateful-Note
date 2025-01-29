package com.example.gratefulnote.addNewDiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.common.diary.domain.model.DiaryUserInput
import com.example.gratefulnote.common.diary.domain.repository.IDiaryRepository
import com.example.gratefulnote.common.domain.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddNewDiaryViewModel @Inject constructor(
    private val repository: IDiaryRepository,
) : ViewModel() {

    fun insert(
        title: String,
        description: String,
        tag: String,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveDiary(DiaryUserInput(
                id = 0L,
                description = description,
                title = title,
                tag = tag,
            )).collect { response ->
                if (response is ResponseWrapper.Succeed)
                    withContext(Dispatchers.Main) {
                        _backToMain.value = true
                    }
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