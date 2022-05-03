package com.example.gratefulnote.mainfragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val dataSource : PositiveEmotionDatabaseDao) : ViewModel(){
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /* Menghapus item kalau tong sampah dipencet */
    fun delete(id : Long){
        viewModelScope.launch {
            dataSource.delete(id)
        }
    }


    /* Mengatur perpindahan fragment ke AddGratitude */
    private val _eventMoveToAddGratitude = MutableLiveData(false)
    val eventMoveToAddGratitude : LiveData<Boolean>
        get() = _eventMoveToAddGratitude

    fun onClickAddNewGratitude(){
        _eventMoveToAddGratitude.value = true
        viewModelJob.cancel()
    }

    fun doneNavigating(){
        _eventMoveToAddGratitude.value = false
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }



    /* Mengatur Selected Date */
    private val _selectedDateString = MutableLiveData("")
    val selectedDateString : LiveData<String>
        get() = _selectedDateString
    val displayedDateString = Transformations.map(_selectedDateString){
        if (it!!.isEmpty())
            "--/-/----"
        else
            it
    }

    fun setDateString(date : String = "") {
        _selectedDateString.value = date
    }


    /* Semua recyclerView Data */
    private val _recyclerViewDataUpdated = MutableLiveData(false)
    val recyclerViewDataUpdated : LiveData<Boolean>
        get() = _recyclerViewDataUpdated

    private var _recyclerViewData = listOf<PositiveEmotion>()
    val recyclerViewData : List<PositiveEmotion>
        get() = _recyclerViewData

    init {
        getAllPositiveEmotion()

    }

    private fun getAllPositiveEmotion(){
        viewModelScope.launch{
            _recyclerViewData = dataSource.getAllPositiveEmotion(_selectedDateString.value!!)
            Log.d("Debugging" , "${_recyclerViewData.size}")
            Log.d("Debugging2" , "'%${selectedDateString.value!!}%'")
            _recyclerViewDataUpdated.value = true
        }
    }

    fun updateRecyclerViewData(){
        getAllPositiveEmotion()
    }

    fun doneUpdatingRecyclerViewData(){
        _recyclerViewDataUpdated.value = false
    }
}