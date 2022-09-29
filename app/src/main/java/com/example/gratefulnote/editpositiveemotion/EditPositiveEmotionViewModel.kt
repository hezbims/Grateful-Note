package com.example.gratefulnote.editpositiveemotion

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPositiveEmotionViewModel(app : Application , private val id : Long) : AndroidViewModel(app) {

    private val dao = PositiveEmotionDatabase.getInstance(app).positiveEmotionDatabaseDao

    fun getCurrentPositiveEmotion() =
        viewModelScope.async(Dispatchers.IO) { dao.getAPositiveEmotion(id) }

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
                EditPositiveEmotionArgs.fromBundle(fragment.requireArguments()).positiveEmotionId
            )

            return ViewModelProvider(
                fragment ,
                viewModelFactory)[EditPositiveEmotionViewModel::class.java]
        }
    }

}