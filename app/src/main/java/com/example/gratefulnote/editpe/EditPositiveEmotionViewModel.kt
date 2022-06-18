package com.example.gratefulnote.editpe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.*

class EditPositiveEmotionViewModel(private val database : PositiveEmotionDatabaseDao ,
                                   private val id : Long
                                   ) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    val curPositiveEmotion = scope.async{ database.getAPositiveEmotion(id) }

    private val _navigateBack = MutableLiveData(false)
    val navigateBack : LiveData<Boolean>
        get() = _navigateBack

    var isFirstTimeCreated = true

    fun updatePositiveEmotion(what : String , why : String){
        scope.launch{
            database.updateData(what , why , id)
            withContext(Dispatchers.Main) {
                _navigateBack.value = true
            }
        }
    }
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

}