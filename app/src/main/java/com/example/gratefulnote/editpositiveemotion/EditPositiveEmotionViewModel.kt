package com.example.gratefulnote.editpositiveemotion

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPositiveEmotionViewModel(
    app : Application ,
    currentPositiveEmotion : PositiveEmotion
    ) : AndroidViewModel(app) {

    private val dao = PositiveEmotionDatabase.getInstance(app).positiveEmotionDatabaseDao

    private var _currentPositiveEmotion = currentPositiveEmotion
    val currentPositiveEmotion : PositiveEmotion
        get() = _currentPositiveEmotion

    fun updatePositiveEmotion(newPositiveEmotion : PositiveEmotion){
        _currentPositiveEmotion = newPositiveEmotion
        viewModelScope.launch(Dispatchers.IO){
            dao.normalUpdate(newPositiveEmotion)
        }
    }

    companion object{
        fun getViewModel(fragment : Fragment) : EditPositiveEmotionViewModel{
            val viewModelFactory = EditPositiveEmotionViewModelFactory(
                fragment.requireActivity().application,
                EditPositiveEmotionArgs.fromBundle(fragment.requireArguments()).currentPositiveEmotion
            )

            return ViewModelProvider(
                fragment ,
                viewModelFactory)[EditPositiveEmotionViewModel::class.java]
        }
    }

}

class EditPositiveEmotionViewModelFactory(
    private val app : Application,
    private val currentPositiveEmotion : PositiveEmotion
    )
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPositiveEmotionViewModel::class.java))
            return EditPositiveEmotionViewModel(app, currentPositiveEmotion) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}