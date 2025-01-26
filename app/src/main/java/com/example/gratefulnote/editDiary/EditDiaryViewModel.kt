package com.example.gratefulnote.editDiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.common.diary.domain.repository.IDiaryRepository
import com.example.gratefulnote.common.domain.ResponseWrapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditDiaryViewModel.Factory::class)
class EditDiaryViewModel @AssistedInject constructor(
    private val repository: IDiaryRepository,
    @Assisted private val diaryId: Long
) : ViewModel() {

    private var _currentDiaryResponse : MutableStateFlow<ResponseWrapper<DiaryDetails>?> = MutableStateFlow(ResponseWrapper.Loading())
    val currentDiaryResponse : StateFlow<ResponseWrapper<DiaryDetails>?>
        get() = _currentDiaryResponse.onStart {
            loadDiaryDetails()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ResponseWrapper.Loading()
        )
    private var currentDiaryDetails: DiaryDetails? = null

    fun doneCollectCurrentDiary(){
        _currentDiaryResponse.update { null }
    }

    private fun loadDiaryDetails(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDiaryDetails(diaryId).collect { diaryResponse ->
                _currentDiaryResponse.update {
                    diaryResponse
                }
                if (diaryResponse is ResponseWrapper.Succeed)
                    currentDiaryDetails = diaryResponse.data
            }
        }
    }

    fun updateDiaryDetails(
        what : String? = null,
        why : String? = null,
    ){
        currentDiaryDetails?.let { diaryDetails ->
            val updatedDiary = diaryDetails.copy(
                title = what ?: diaryDetails.title,
                description = why ?: diaryDetails.description
            )

            if (updatedDiary == diaryDetails)
                return@let

            viewModelScope.launch(Dispatchers.IO) {
                repository.updateDetails(updatedDiary).collect {
                    _hasNewDataAfterEdit = true
                }
            }

            currentDiaryDetails = updatedDiary
        }
    }

    private var _hasNewDataAfterEdit = false
    val hasNewDataAfterEdit : Boolean
        get() = _hasNewDataAfterEdit

    fun doneHandlingHasNewData() {
        _hasNewDataAfterEdit = false
    }

    @AssistedFactory
    interface Factory {
        fun create(diaryId: Long) : EditDiaryViewModel
    }
}