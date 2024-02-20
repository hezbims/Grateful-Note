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
            is DailyNotificationEvent.OnCreateNewDailyNotification ->
                createNewDailyNotification(hour = event.hour, minute = event.minute)
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
    private fun createNewDailyNotification(hour : Int , minute: Int){
        viewModelScope.launch(Dispatchers.IO) {
            dailyNotificationManager.addNewDailyNotification(
                hour = hour, minute = minute,
            ).collect { response ->
                // TODO : Tambahin method untuk ngedisplay toast kalo ada response error
                _state.update {
                    val openTimePickerDialog =
                        if (response is ResponseWrapper.ResponseSucceed) false
                        else _state.value.openTimePickerDialog

                    it.copy(
                        createNewDailyNotificationStatus = response,
                        openTimePickerDialog = openTimePickerDialog,
                    )
                }
            }
        }
    }
}

data class DailyNotificationState(
    val listDailyNotification : ResponseWrapper<List<DailyNotification>> = ResponseWrapper.ResponseLoading(),
    val createNewDailyNotificationStatus : ResponseWrapper<Long> = ResponseWrapper.ResponseSucceed(),
    val openTimePickerDialog : Boolean = false,
)

sealed class DailyNotificationEvent {
    data object OnOpenTimePickerDialog : DailyNotificationEvent()
    data object OnDismissTimePickerDialog : DailyNotificationEvent()
    data object OnLoadListNotification : DailyNotificationEvent()
    class OnCreateNewDailyNotification(val minute : Int, val hour : Int) : DailyNotificationEvent()
}