package com.example.gratefulnote.editpositiveemotion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel(assistedFactory = EditPositiveEmotionViewModel.Factory::class)
class EditPositiveEmotionViewModel @AssistedInject constructor(
    private val dao : PositiveEmotionDao,
    @Assisted currentPositiveEmotion : PositiveEmotion
) : ViewModel() {
    private var _currentPositiveEmotion = currentPositiveEmotion
    val currentPositiveEmotion : PositiveEmotion
        get() = _currentPositiveEmotion

    fun updatePositiveEmotion(
        what : String? = null,
        why : String? = null,
    ){
        _currentPositiveEmotion = _currentPositiveEmotion.copy(
            what = what ?: _currentPositiveEmotion.what,
            why = why ?: _currentPositiveEmotion.why,
            updatedAt = System.currentTimeMillis(),
        )
        viewModelScope.launch(Dispatchers.IO){
            dao.normalUpdate(_currentPositiveEmotion)
            withContext(Dispatchers.Main) {
                _hasNewDataAfterEdit.value = true
            }
        }
    }

    private val _hasNewDataAfterEdit = MutableLiveData(false)
    val hasNewDataAfterEdit : LiveData<Boolean> get() = _hasNewDataAfterEdit
    fun doneHandlingHasNewData(){
        _hasNewDataAfterEdit.value = false
    }

    @AssistedFactory
    interface Factory {
        fun create(currentPositiveEmotion: PositiveEmotion) : EditPositiveEmotionViewModel
    }
}