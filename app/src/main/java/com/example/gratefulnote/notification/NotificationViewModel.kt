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

    fun setSavedClock(inputClock : Clock){
        viewModelScope.launch(Dispatchers.IO) {
                with(sharedPreferences.edit()) {
                    putInt(app.getString(R.string.saved_hours) , inputClock.hours)
                    putInt(app.getString(R.string.saved_minutes) , inputClock.minutes)
                    commit()
                }
                withContext(Dispatchers.Main) {
                    _clockDisplay.value = inputClock.format()
                    setAlarmNotification()
                    closeDialog()
                }
        }
    }

    private val _clockDisplay = MutableLiveData(
        Clock.getSavedClock(app).format()
    )
    val clockDisplay : LiveData<String>
        get() = _clockDisplay

    private val alarmSetter = NotificationAlarmSetter(app)

    fun setAlarmNotification(){
        if (isSwitchChecked)
            alarmSetter.setAlarm()
        else
            alarmSetter.cancel()
    }




    private var _isSwitchChecked = sharedPreferences.getBoolean(
        app.getString(R.string.saved_switch_status),
        false
    )
    val isSwitchChecked : Boolean
        get() = _isSwitchChecked
    fun updateSwitchStatus(isChecked : Boolean){
        _isSwitchChecked = isChecked
        with(sharedPreferences.edit()) {
            putBoolean(
                app.getString(R.string.saved_switch_status),
                isChecked
            )
            apply()
        }
    }




    private var _isDialogOpened = false
    val isDialogOpened : Boolean
        get() = _isDialogOpened
    fun openDialog(){_isDialogOpened = true}
    fun closeDialog(){_isDialogOpened = false}
}

class NotificationViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java))
            return NotificationViewModel(application) as T
        throw IllegalArgumentException("Unknown NotificationViewModel")
    }
}
