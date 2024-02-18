package com.example.gratefulnote.mainfragment

import android.app.Application
import androidx.lifecycle.*
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.GratefulNoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val app : Application) : AndroidViewModel(app){
    private val dataSource = GratefulNoteDatabase.getInstance(app.applicationContext)
        .positiveEmotionDao

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
            updateRecyclerViewData(_filterState.value!!)
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
    private var _recyclerViewData = MutableLiveData<List<PositiveEmotion>?>()
    val recyclerViewData : LiveData<List<PositiveEmotion>?>
        get() = _recyclerViewData

    fun updateRecyclerViewData(currentFilterState : FilterState){
        viewModelScope.launch(Dispatchers.Main) {
            _recyclerViewData.value =
                withContext(Dispatchers.IO) {
                    val nextData = currentFilterState.let {
                        dataSource.getAllPositiveEmotion(
                            month = it.selectedMonth,
                            year = it.selectedYear,
                            type = it.selectedPositiveEmotion,
                            onlyFavorite = it.onlyFavorite
                        )
                    }


                    if (_filterState.value!!.switchState)
                        nextData.reversed()
                    else nextData
                }
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