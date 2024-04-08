package com.example.gratefulnote.daily_notification.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.daily_notification.domain.service.IDailyNotificationManager
import com.example.gratefulnote.database.DailyNotificationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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
            DailyNotificationEvent.OnBackPressWhenMultiSelectModeActivated ->
                disableMultiSelectMode()
            DailyNotificationEvent.OnConfirmDeleteDialog ->
                deleteSelectedItems()
            DailyNotificationEvent.OnDismissDeleteDialog ->
                dismissDeleteDialog()
            DailyNotificationEvent.OnClickTrashButton ->
                showDeleteDialog()
            is DailyNotificationEvent.OnCreateNewDailyNotification ->
                createNewDailyNotification(hour = event.hour, minute = event.minute)
            is DailyNotificationEvent.OnLongClickDailyNotificationCard ->
                activateMultipleSelectMode(event.dailyNotification)
            is DailyNotificationEvent.OnClickItemWhenMultiSelectModeActivated ->
                onClickItemWhenSelectModeActivated(event.dailyNotification)
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

    private fun showDeleteDialog(){
        _state.update { it.copy(openConfirmDeleteDialog = true) }
    }

    private fun dismissDeleteDialog(){
        _state.update { it.copy(openConfirmDeleteDialog = false) }
    }

    private fun disableMultiSelectMode(){
        val uncheckedList = _state.value.listDailyNotification.map {
            it.copy(isSelectedForDeleteCandidate = false)
        }
        _state.update { it.copy(
            listDailyNotification = uncheckedList,
            isMultiSelectModeActivated = false,
        ) }
    }

    private fun deleteSelectedItems(){
        _state.update { it.copy(openConfirmDeleteDialog = false) }
        viewModelScope.launch (Dispatchers.IO) {
            dailyNotificationManager.deleteDailyNotification(
                _state.value.listDailyNotification
                    .filter {
                        it.isSelectedForDeleteCandidate
                    }
                    .map {
                        it.data
                    }
            ).collect {
                if (it !is ResponseWrapper.ResponseLoading)
                    loadListNotification()
            }
        }
    }

    private fun onClickItemWhenSelectModeActivated(
        dailyNotification : DailyNotificationUiModel
    ){
        val newList =
        if (dailyNotification.isSelectedForDeleteCandidate){
            removeItemToDeleteCandidate(item = dailyNotification.data)
        } else {
            addItemToDeleteCandidate(item = dailyNotification.data)
        }

        _state.update { it.copy(listDailyNotification = newList) }
    }

    private fun addItemToDeleteCandidate(
        item: DailyNotificationEntity,
    ) : List<DailyNotificationUiModel> {
        return onEditItemFromDeleteCandidate(item = item, willRemovedFromDeleteCandidate = false)
    }

    private fun removeItemToDeleteCandidate(
        item: DailyNotificationEntity,
    ) : List<DailyNotificationUiModel> {
        return onEditItemFromDeleteCandidate(item = item, willRemovedFromDeleteCandidate = true)
    }

    private fun onEditItemFromDeleteCandidate(
        item : DailyNotificationEntity,
        willRemovedFromDeleteCandidate : Boolean,
    ) : List<DailyNotificationUiModel> {
        val listData = _state.value.listDailyNotification.map {
            if (it.data.id == item.id) {
                it.copy(isSelectedForDeleteCandidate = !willRemovedFromDeleteCandidate)
            }
            else it
        }
        return listData
    }

    private fun activateMultipleSelectMode(dailyNotification: DailyNotificationEntity){
        if (_state.value.isMultiSelectModeActivated)
            return

        val newListData = addItemToDeleteCandidate(dailyNotification)

        _state.update {
            it.copy(
                isMultiSelectModeActivated = true,
                listDailyNotification = newListData,
            )
        }
    }

    private fun loadListNotification(){
        viewModelScope.launch(Dispatchers.IO) {
            dailyNotificationManager.getAllDailyNotification()
            .filter {
                it is ResponseWrapper.ResponseSucceed
            }
            .map { response ->
                (response as ResponseWrapper.ResponseSucceed).data!!
                    .map {
                        DailyNotificationUiModel(
                            data = it,
                            isSelectedForDeleteCandidate = false,
                        )
                    }
            }
            .collect { listData ->
                _state.update {
                    it.copy(listDailyNotification = listData)
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
                if (response is ResponseWrapper.ResponseSucceed)
                    loadListNotification()
            }
        }
    }
}

data class DailyNotificationState(
    val listDailyNotification : List<DailyNotificationUiModel> = emptyList(),
    val createNewDailyNotificationStatus : ResponseWrapper<Int> = ResponseWrapper.ResponseSucceed(),
    val openTimePickerDialog : Boolean = false,
    val openConfirmDeleteDialog : Boolean = false,
    val isMultiSelectModeActivated : Boolean = false,
)

sealed class DailyNotificationEvent {
    data object OnOpenTimePickerDialog : DailyNotificationEvent()
    data object OnDismissTimePickerDialog : DailyNotificationEvent()
    data object OnLoadListNotification : DailyNotificationEvent()
    data object OnBackPressWhenMultiSelectModeActivated : DailyNotificationEvent()
    data object OnClickTrashButton : DailyNotificationEvent()
    data object OnConfirmDeleteDialog : DailyNotificationEvent()
    data object OnDismissDeleteDialog : DailyNotificationEvent()
    class OnCreateNewDailyNotification(val minute : Int, val hour : Int) : DailyNotificationEvent()
    class OnLongClickDailyNotificationCard(val dailyNotification: DailyNotificationEntity) : DailyNotificationEvent()
    class OnClickItemWhenMultiSelectModeActivated(val dailyNotification : DailyNotificationUiModel) : DailyNotificationEvent()
}