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

    private fun updateRecyclerViewData(){
        viewModelScope.launch(Dispatchers.Main) {
            _recyclerViewData.value =
                withContext(Dispatchers.IO) {
                    with(
                        dataSource.getAllPositiveEmotion(
                            _selectedMonth,
                            _selectedYear,
                            _selectedPositiveEmotion
                        )
                    ) {
                        if (switchState) reversed() else this
                    }
                }
        }
    }


    private val months: Array<String> = app.applicationContext.resources.getStringArray(R.array.months_list)
    private var _selectedMonth = 0
    val selectedMonth : String
        get() = months[_selectedMonth]


    private var _selectedYear : Int? = null
    val selectedYear : String
        get() = _selectedYear?.toString() ?: getString(R.string.semua)

    private var _switchState = true // Terbaru
    val switchState : Boolean
        get() = _switchState

    private var _selectedPositiveEmotion = getString(R.string.semua)
    val selectedPositiveEmotion : String
        get() = _selectedPositiveEmotion

    fun setFilterData(month : String , year : String ,
        typeOfPE : String , newSwitchState : Boolean){
        _switchState = newSwitchState
        _selectedMonth = months.indexOf(month)
        _selectedPositiveEmotion = typeOfPE
        _selectedYear = if (year == getString(R.string.semua)) null else year.toInt()
        updateRecyclerViewData()
    }


    private val listOfYear = dataSource.getAllYear()
    val listOfYearToString = Transformations.map(listOfYear){l ->
        if (l == null)
            emptyList()
        else
            listOf(getString(R.string.semua)) + l.map{it.toString()}
    }

    private var _canShowFilterDialog = true
    val canShowFilterDialog : Boolean
        get() = _canShowFilterDialog
    fun cannotShowFilterDialog(){_canShowFilterDialog = false}

    private var _isDialogViewFirstTimeCreated = true
    val isDialogViewFirstTimeCreated : Boolean
        get() = _isDialogViewFirstTimeCreated
    fun doneCreatingFirstViewDialog(){_isDialogViewFirstTimeCreated = false}

    fun resetDialogState(){
        _isDialogViewFirstTimeCreated = true
        _canShowFilterDialog = true
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