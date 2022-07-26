package com.example.gratefulnote.notification

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.gratefulnote.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel(private val app : Application) : AndroidViewModel(app){
    private val sharedPreferences = app.getSharedPreferences(
        app.getString(R.string.shared_preferences_name) ,
        Context.MODE_PRIVATE)

    class Clock(private val hours : Int, private val minutes : Int){
        fun format() =
            "${timeFormat(hours)}:${timeFormat(minutes)}"

        private fun timeFormat(time : Int) =
            if (time < 10) "0$time" else time.toString()
    }

    private val liveClock = MutableLiveData(getSavedClock())

    fun setLiveClock(hours : Int , minutes : Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                with(sharedPreferences.edit()) {
                    putInt(
                        app.getString(R.string.saved_hours),
                        hours
                    )
                    putInt(
                        app.getString(R.string.saved_minutes),
                        minutes
                    )
                    commit()
                }
                withContext(Dispatchers.Main) {
                    liveClock.value = getSavedClock()
                    setDialogOpenedStatus(false)
                }
            }
        }
    }

    private fun getSavedClock() = Clock(
            sharedPreferences.getInt(app.getString(R.string.saved_hours), 0),
            sharedPreferences.getInt(app.getString(R.string.saved_minutes) , 0)
        )

    val clockDisplay = Transformations.map(liveClock){ it.format() }


    private val _isSwitchChecked = MutableLiveData(sharedPreferences.getBoolean(
        app.getString(R.string.saved_switch_status),
        false))
    val isSwitchChecked : LiveData<Boolean>
        get() = _isSwitchChecked
    fun updateSwitchStatus(isChecked : Boolean){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                with(sharedPreferences.edit()) {
                    putBoolean(
                        app.getString(R.string.saved_switch_status),
                        isChecked
                    )
                    commit()
                }
                withContext(Dispatchers.Main){
                    _isSwitchChecked.value = isChecked
                }
            }
        }
    }

    private var _isDialogOpened = false
    val isDialogOpened : Boolean
        get() = _isDialogOpened
    fun setDialogOpenedStatus(status : Boolean){
        _isDialogOpened = status
    }
}

class NotificationViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java))
            return NotificationViewModel(application) as T
        throw IllegalArgumentException("Unknown NotificationViewModel")
    }
}
