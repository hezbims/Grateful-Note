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
                    _clockDisplay.value = Clock(hours , minutes).format()
                    setAlarmNotification()
                    setDialogOpenedStatus(false)
                }
            }
        }
    }

    private val _clockDisplay = MutableLiveData(Clock.getSavedClock(
            sharedPreferences ,
            app.applicationContext
        ).format()
    )
    val clockDisplay : LiveData<String>
        get() = _clockDisplay

    private val alarmSetter = NotificationAlarmSetter(app.applicationContext)

    fun setAlarmNotification(){
        if (isSwitchChecked.value == true)
            alarmSetter.setAlarm()
        else
            alarmSetter.cancel()
    }




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
