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
            clickEdit = true
            updateRecyclerViewData()
        }
    }

    fun normalUpdate(positiveEmotion: PositiveEmotion){
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.normalUpdate(positiveEmotion)
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
    private var _recyclerViewData = listOf<PositiveEmotion>()
    val recyclerViewData : List<PositiveEmotion>
        get() = _recyclerViewData
    private val _isDataUpdated = MutableLiveData(false)
    val isDataUpdated : LiveData<Boolean>
        get() = _isDataUpdated
    fun doneCreatingRecyclerView(){_isDataUpdated.value = false}

    fun updateRecyclerViewData(){
        viewModelScope.launch(Dispatchers.Main) {
            _recyclerViewData =
                withContext(Dispatchers.IO) {
                    with(
                        dataSource.getAllPositiveEmotion(
                            month = _filterState.value!!.selectedMonth,
                            year = _filterState.value!!.selectedYear,
                            type = _filterState.value!!.selectedPositiveEmotion,
                            onlyFavorite = _filterState.value!!.onlyFavorite
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
        typeOfPE : String , newSwitchState : Boolean , onlyFavorite : Boolean){
        _filterState.value = FilterState.getInstance(
            newSelectedMonth = month ,
            newSelectedYear = year ,
            newSelectedPositiveEmotion = typeOfPE ,
            newSwitchState = newSwitchState ,
            newOnlyFavorite = onlyFavorite,
            context = app.applicationContext
        )
    }


    private val listOfYear = dataSource.getAllYear()
    val listOfYearToString = listOfYear.map{intYear ->
        listOf(getString(R.string.semua)) + intYear.map{it.toString()}
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

class MainViewModelFactory(
    private val app: Application
    ) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(app) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}