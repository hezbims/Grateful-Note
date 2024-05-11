package com.example.gratefulnote.mainfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.database.PositiveEmotion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app : Application) : AndroidViewModel(app){
    private val dao = GratefulNoteDatabase.getInstance(app.applicationContext)
        .positiveEmotionDao

    private var deletedItemId = 0L
    fun setDeletedItemId(id : Long){ deletedItemId = id }
    /* Menghapus item kalau tong sampah dipencet */
    fun delete(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(deletedItemId)
            clickEdit = true
            fetchRecyclerViewDataWithCurrentFilterState()
        }
    }

    fun normalUpdate(positiveEmotion: PositiveEmotion){
        viewModelScope.launch(Dispatchers.IO) {
            dao.normalUpdate(positiveEmotion)
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


    /* Semua recyclerView Data */
    private var _recyclerViewData = MutableLiveData<List<PositiveEmotion>?>()
    val recyclerViewData : LiveData<List<PositiveEmotion>?>
        get() = _recyclerViewData

    private fun fetchRecyclerViewDataWithCurrentFilterState(){
        viewModelScope.launch(Dispatchers.Main) {
            _recyclerViewData.value =
                withContext(Dispatchers.IO) {
                    dao.getAllPositiveEmotion(
                        month = _filterState.selectedMonth,
                        year = _filterState.selectedYear,
                        type = _filterState.positiveEmotionType,
                        onlyFavorite = _filterState.isOnlyFavorite,
                        isSortedLatest = _filterState.isSortedLatest,
                    )
                }
        }
    }

    private var _filterState = FilterState()
    val filterState : FilterState
        get() = _filterState
    var clickEdit = false

    fun setNewFilterData(newFilterState: FilterState){
        _filterState = newFilterState
        fetchRecyclerViewDataWithCurrentFilterState()
    }


    val listOfYear = dao.getAllYear()
}