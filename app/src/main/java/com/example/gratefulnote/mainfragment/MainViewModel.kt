package com.example.gratefulnote.mainfragment

import androidx.lifecycle.*
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabaseDao
import kotlinx.coroutines.launch

class MainViewModel(private val dataSource : PositiveEmotionDatabaseDao) : ViewModel(){
    val typeOfPositiveEmotion = arrayOf(
        "All" , "Joy", "Gratitude", "Serenity", "Interest", "Hope",
        "Pride", "Amusement", "Inspiration", "Awe", "Love", "Other"
    )

    /* Menghapus item kalau tong sampah dipencet */
    fun delete(id : Long){
        viewModelScope.launch {
            dataSource.delete(id)
            updateRecyclerViewData()
        }
    }


    /* Mengatur perpindahan fragment ke AddGratitude */
    private val _eventMoveToAddGratitude = MutableLiveData(false)
    val eventMoveToAddGratitude : LiveData<Boolean>
        get() = _eventMoveToAddGratitude

    fun onClickAddNewGratitude(){
        _eventMoveToAddGratitude.value = true
    }

    fun doneNavigating(){
        _eventMoveToAddGratitude.value = false
    }



    /* Mengatur Selected Date */
    private val _selectedDateString = MutableLiveData("")
    val selectedDateString : LiveData<String>
        get() = _selectedDateString
    val displayedDateString = Transformations.map(_selectedDateString){
        if (it!!.isEmpty())
            "All"
        else
            it
    }

    fun setDateString(date : String = "") {
        _selectedDateString.value = date
    }


    /* Mengatur data di menus */
    private val _selectedPositiveEmotion = MutableLiveData("All")
    val selectedPositiveEmotion : LiveData<String>
        get() = _selectedPositiveEmotion
    fun setSelectedPositiveEmotion(data : String){
        _selectedPositiveEmotion.value = data
    }


    /* Semua recyclerView Data */
    private val _recyclerViewData = MutableLiveData(listOf<PositiveEmotion>())
    val recyclerViewData : LiveData<List<PositiveEmotion>>
        get() = _recyclerViewData

    val isDataEmpty : Boolean
        get(){
            _recyclerViewData.value?.let {
                return it.isEmpty()
            }
            return true
        }


    init{
        updateRecyclerViewData()
    }

    private suspend fun getAllPositiveEmotion() =
        dataSource.getAllPositiveEmotion(
            if (_selectedPositiveEmotion.value!! == "All")
                ""
            else
                _selectedPositiveEmotion.value!!
        )

    fun updateRecyclerViewData(){
        viewModelScope.launch {
            _recyclerViewData.value = getAllPositiveEmotion()
        }
    }
}