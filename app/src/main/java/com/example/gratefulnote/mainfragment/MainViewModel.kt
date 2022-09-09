package com.example.gratefulnote.mainfragment

import android.app.Application
import androidx.lifecycle.*
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val app : Application) : AndroidViewModel(app){
    private val dataSource = PositiveEmotionDatabase.getInstance(app.applicationContext)
        .positiveEmotionDatabaseDao

    val typeOfPositiveEmotion : Array<String> =
                arrayOf(getString(R.string.semua)) +
                app.applicationContext
                .resources
                .getStringArray(R.array.type_of_positive_emotion_array)

    private var deletedItemId = 0L
    fun setDeletedItemId(id : Long){ deletedItemId = id }
    /* Menghapus item kalau tong sampah dipencet */
    fun delete(){
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.delete(deletedItemId)
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


    /* Semua recyclerView Data */
    private var _recyclerViewData = MutableLiveData(listOf<PositiveEmotion>())
    val recyclerViewData : LiveData<List<PositiveEmotion>>
        get() = _recyclerViewData
    private val _isDataUpdated = MutableLiveData(false)
    val isDataUpdated : LiveData<Boolean>
        get() = _isDataUpdated
    fun doneCreatingRecyclerView(){_isDataUpdated.value = false}

    fun updateRecyclerViewData(){
        viewModelScope.launch(Dispatchers.Main) {
            _recyclerViewData.value =
                withContext(Dispatchers.IO) {
                    with(
                        dataSource.getAllPositiveEmotion(
                            _filterState.value!!.selectedMonth,
                            _filterState.value!!.selectedYear,
                            _filterState.value!!.selectedPositiveEmotion
                        )
                    ) {
                        if (_filterState.value!!.switchState) reversed() else this
                    }
                }
            _isDataUpdated.value = true
        }
    }

    private val _filterState = MutableLiveData(FilterState(app.applicationContext))
    val filterState : LiveData<FilterState>
        get() = _filterState
    var clickEdit = false

    fun setFilterData(month : String , year : String ,
        typeOfPE : String , newSwitchState : Boolean){
        _filterState.value = FilterState.getInstance(
            month , year ,
            typeOfPE , newSwitchState ,
            app.applicationContext
        )
    }


    private val listOfYear = dataSource.getAllYear()
    val listOfYearToString = Transformations.map(listOfYear){l ->
        if (l == null)
            emptyList()
        else
            listOf(getString(R.string.semua)) + l.map{it.toString()}
    }

    fun getString(id : Int) =
        app.applicationContext.getString(id)

    companion object{
        fun getInstance(app: Application, owner: ViewModelStoreOwner) =
            ViewModelProvider(
                owner , MainViewModelFactory(app)
            )[MainViewModel::class.java]
    }
}