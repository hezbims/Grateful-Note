package com.example.gratefulnote.mainMenu.presentation.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gratefulnote.common.diary.domain.model.FilterState
import com.example.gratefulnote.common.diary.domain.repository.IDiaryRepository
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.database.DiaryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IDiaryRepository
) : ViewModel() {
    /**
     * Mengatur perpindahan ke fragment lain
     **/
    private val _navEvent = MutableLiveData<MainFragmentNavEvent?>()
    val navEvent : LiveData<MainFragmentNavEvent?>
        get() = _navEvent

    fun onNavEvent(newEvent : MainFragmentNavEvent){
        _navEvent.value = newEvent
    }
    fun doneNavigating(){
        _navEvent.value = null
    }


    private var _pagingSource : DiaryPreviewPagingSource? = null
    private val _filterState = MutableStateFlow(FilterState())
    val filterState : StateFlow<FilterState>
        get() = _filterState
    @OptIn(ExperimentalCoroutinesApi::class)
    val recycleViewPager = _filterState.flatMapLatest {
        Pager(
            config = PagingConfig(pageSize = DiaryDao.PAGE_SIZE)
        ){
            DiaryPreviewPagingSource(
                repository = repository,
                filterState = it
            ).let {
                _pagingSource = it
                it
            }
        }.flow.cachedIn(viewModelScope)
    }

    fun setNewFilterData(newFilterState: FilterState){
        _filterState.value = newFilterState
    }


    private var deletedItemId = 0L
    fun setDeletedItemId(id : Long){ deletedItemId = id }

    fun onDelete(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(deletedItemId).collect { response ->
                if (response is ResponseWrapper.Succeed)
                    refreshDiaryList()
            }
        }
    }

    fun onToogleIsFavorite(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.toogleIsFavorite(id)
            refreshDiaryList()
        }
    }

    private var _doScrollToTop = false
    val doScrollToTop : Boolean
        get() = _doScrollToTop
    fun doneScrollToTop() { _doScrollToTop = false }

    fun refreshDiaryList(scrollToTop: Boolean = false){
        _pagingSource?.invalidate()
        _doScrollToTop = scrollToTop
    }

    val listOfYear = repository.getDistinctYears()
}

sealed class MainFragmentNavEvent {
    data object MoveToAddGratitude : MainFragmentNavEvent()
    data object OpenFilterDialog : MainFragmentNavEvent()
    data class ToEditDiary(val diaryId: Long) : MainFragmentNavEvent()
}