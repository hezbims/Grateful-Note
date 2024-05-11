package com.example.gratefulnote.editpositiveemotion

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

@HiltViewModel(assistedFactory = EditPositiveEmotionViewModel.Factory::class)
class EditPositiveEmotionViewModel @AssistedInject constructor(
    private val dao : PositiveEmotionDao,
    @Assisted currentPositiveEmotion : PositiveEmotion
) : ViewModel() {
    private var _currentPositiveEmotion = currentPositiveEmotion
    val currentPositiveEmotion : PositiveEmotion
        get() = _currentPositiveEmotion

    fun updatePositiveEmotion(newPositiveEmotion : PositiveEmotion){
        _currentPositiveEmotion = newPositiveEmotion
        viewModelScope.launch(Dispatchers.IO){
            dao.normalUpdate(newPositiveEmotion)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(currentPositiveEmotion: PositiveEmotion) : EditPositiveEmotionViewModel
    }
}