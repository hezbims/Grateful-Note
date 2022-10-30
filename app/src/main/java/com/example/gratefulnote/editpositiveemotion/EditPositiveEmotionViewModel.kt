package com.example.gratefulnote.editpositiveemotion

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPositiveEmotionViewModel(
    app : Application ,
    val currentPositiveEmotion : PositiveEmotion
    ) : AndroidViewModel(app) {

    private val dao = PositiveEmotionDatabase.getInstance(app).positiveEmotionDatabaseDao



    private val _navigateBack = MutableLiveData(false)
    val navigateBack : LiveData<Boolean>
        get() = _navigateBack
    fun navigateBack(){_navigateBack.value = true}
    fun doneNavigateBack(){ _navigateBack.value = false }

    fun updatePositiveEmotion(newPositiveEmotion : PositiveEmotion){
        viewModelScope.launch(Dispatchers.IO){
            dao.normalUpdate(newPositiveEmotion)
            withContext(Dispatchers.Main) {
                navigateBack()
            }
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