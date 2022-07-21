@file:Suppress("UNCHECKED_CAST")

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

    private val _notificationTimeDisplay = MutableLiveData(sharedPreferences.getString(
        app.getString(R.string.saved_notification_time),
        app.getString(R.string.default_notification_time)
    ))
    val notificationTimeDisplay : LiveData<String?>
        get() = _notificationTimeDisplay

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
}

class NotificationViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java))
            return NotificationViewModel(application) as T
        throw IllegalArgumentException("Unknown NotificationViewModel")
    }
}
