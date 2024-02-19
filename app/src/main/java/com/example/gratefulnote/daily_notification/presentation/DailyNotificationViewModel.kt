package com.example.gratefulnote.daily_notification.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.database.DailyNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyNotificationViewModel @Inject constructor(
    private val dailyNotificationManager: IDailyNotificationManager
) : ViewModel(){
    private val _state = MutableStateFlow(DailyNotificationState())
    val state = _state.asStateFlow()

    fun onEvent(event : DailyNotificationEvent){
        when(event){
            DailyNotificationEvent.OnOpenTimePickerDialog ->
                onOpenTimePickerDialog()
            DailyNotificationEvent.OnDismissTimePickerDialog ->
                onDismissTimePickerDialog()
            DailyNotificationEvent.OnLoadListNotification ->
                loadListNotification()
        }
    }
    init {
        onEvent(DailyNotificationEvent.OnLoadListNotification)
    }
    private fun onOpenTimePickerDialog(){
        _state.update { it.copy(openTimePickerDialog = true) }
    }
    private fun onDismissTimePickerDialog(){
        _state.update { it.copy(openTimePickerDialog = false) }
    }

    private fun loadListNotification(){
        viewModelScope.launch(Dispatchers.IO) {
            dailyNotificationManager.getAllDailyNotification().collect { response ->
                _state.update {
                    it.copy(listDailyNotification = response)
                }
            }
        }
    }
}

data class DailyNotificationState(
    val listDailyNotification : ResponseWrapper<List<DailyNotification>> = ResponseWrapper.ResponseLoading(),
    val openTimePickerDialog : Boolean = false,
)

sealed class DailyNotificationEvent {
    data object OnOpenTimePickerDialog : DailyNotificationEvent()
    data object OnDismissTimePickerDialog : DailyNotificationEvent()
    data object OnLoadListNotification : DailyNotificationEvent()
}