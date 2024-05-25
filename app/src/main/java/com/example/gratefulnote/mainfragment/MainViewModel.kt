package com.example.gratefulnote.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dao: PositiveEmotionDao
) : ViewModel() {
    private var deletedItemId = 0L
    fun setDeletedItemId(id : Long){ deletedItemId = id }
    /* Menghapus item kalau tong sampah dipencet */
    fun delete(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(deletedItemId)
            fetchRecyclerViewDataWithCurrentFilterState()
        }
    }

    fun normalUpdate(positiveEmotion: PositiveEmotion){
        viewModelScope.launch(Dispatchers.IO) {
            dao.normalUpdate(positiveEmotion)
        }
    }

    /* Mengatur perpindahan fragment ke AddGratitude */
    private val _navEvent = MutableLiveData<MainFragmentNavEvent?>()
    val navEvent : LiveData<MainFragmentNavEvent?>
        get() = _navEvent

    fun onNavEvent(newEvent : MainFragmentNavEvent){
        _navEvent.value = newEvent
    }

    fun doneNavigating(){
        _navEvent.value = null
    }


    /* Semua recyclerView Data */
    private var _recyclerViewState = MutableLiveData(MainRecyclerViewState())
    val recyclerViewData : LiveData<MainRecyclerViewState>
        get() = _recyclerViewState

    fun fetchRecyclerViewDataWithCurrentFilterState(
        scrollToPositionZero: Boolean = false
    ){
        viewModelScope.launch(Dispatchers.Main) {
            _recyclerViewState.value =
                withContext(Dispatchers.IO) {
                    val listData = dao.getAllPositiveEmotion(
                        month = _filterState.selectedMonth,
                        year = _filterState.selectedYear,
                        type = _filterState.positiveEmotionType,
                        onlyFavorite = _filterState.isOnlyFavorite,
                        isSortedLatest = _filterState.isSortedLatest,
                    )
                    val result = _recyclerViewState.value!!.copy(
                        scrollToPositionZero = scrollToPositionZero,
                        listDataResponse = ResponseWrapper.Succeed(data = listData)
                    )
                    result
                }
        }
    }

    fun doneScrollToPositionZero(){
        _recyclerViewState.value = _recyclerViewState.value!!.copy(scrollToPositionZero = false)
    }
    init {
        fetchRecyclerViewDataWithCurrentFilterState()
    }

    private var _filterState = FilterState()
    val filterState : FilterState
        get() = _filterState

    fun setNewFilterData(newFilterState: FilterState){
        _filterState = newFilterState
        fetchRecyclerViewDataWithCurrentFilterState()
    }


    val listOfYear = dao.getAllYear()

    data class MainRecyclerViewState (
        val scrollToPositionZero : Boolean = false,
        val listDataResponse : ResponseWrapper<List<PositiveEmotion>> = ResponseWrapper.Loading()
    )
}

sealed class MainFragmentNavEvent {
    data object MoveToAddGratitude : MainFragmentNavEvent()
    data object OpenFilterDialog : MainFragmentNavEvent()
}