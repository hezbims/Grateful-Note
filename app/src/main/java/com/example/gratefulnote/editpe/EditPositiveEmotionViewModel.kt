package com.example.gratefulnote.editpe

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.*

class EditPositiveEmotionViewModel(private val database : PositiveEmotionDatabaseDao) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    fun getCurrentPositiveEmotion(id : Long) = scope.async {
        database.getAPositiveEmotion(id)
    }

    private val _navigateBack = MutableLiveData(false)
    val navigateBack : LiveData<Boolean>
        get() = _navigateBack
    fun navigateBack(){_navigateBack.value = true}
    fun doneNavigateBack(){ _navigateBack.value = false }

    var isFirstTimeFragmentCreated = true

    fun updatePositiveEmotion(what : String , why : String , id : Long){
        scope.launch{
            database.updateData(what , why , id)
            withContext(Dispatchers.Main) {
                navigateBack()
            }
        }
    }

    private var _isDialogOpened = false
    val isDialogOpened : Boolean
        get() = _isDialogOpened
    fun closeDialog(){_isDialogOpened = false}
    fun openDialog(){_isDialogOpened = true}

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    companion object{
        fun getViewModel(activity: FragmentActivity) : EditPositiveEmotionViewModel{
            val datasource = PositiveEmotionDatabase.getInstance(activity.application)
                .positiveEmotionDatabaseDao
            val viewModelFactory = EditPositiveEmotionViewModelFactory(
                datasource)

            return ViewModelProvider(activity ,
                viewModelFactory)[EditPositiveEmotionViewModel::class.java]
        }
    }

}