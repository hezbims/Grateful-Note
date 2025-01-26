package com.example.gratefulnote.backuprestore.presentation.confirm_restore_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import com.example.gratefulnote.common.domain.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmRestoreDialogViewModel @Inject constructor(
    private val repository : IBackupRestoreManager
) : ViewModel(){
    private val _state = MutableStateFlow(ConfirmRestoreDialogState())
    val state : StateFlow<ConfirmRestoreDialogState>
        get() = _state.asStateFlow()

    fun onEvent(event: ConfirmRestoreDialogEvent){
        when (event){
            ConfirmRestoreDialogEvent.ResetState ->
                _state.update { ConfirmRestoreDialogState() }
            is ConfirmRestoreDialogEvent.InitState ->
                _state.update { ConfirmRestoreDialogState(restoreFile = event.restoreFile) }
            ConfirmRestoreDialogEvent.RestoreConfirmed ->
                restoreCurrentFile()
        }
    }

    private fun restoreCurrentFile(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.restoreFile(_state.value.restoreFile!!.file)
                .collect { response ->
                    _state.update { it.copy(restoreState = response) }
                }
        }
    }
}

data class ConfirmRestoreDialogState(
    val restoreFile : DocumentFileDto? = null,
    val restoreState : ResponseWrapper<Unit>? = null
)

sealed class ConfirmRestoreDialogEvent {
    data object ResetState : ConfirmRestoreDialogEvent()
    class InitState(val restoreFile : DocumentFileDto?) : ConfirmRestoreDialogEvent()
    data object RestoreConfirmed : ConfirmRestoreDialogEvent()

}