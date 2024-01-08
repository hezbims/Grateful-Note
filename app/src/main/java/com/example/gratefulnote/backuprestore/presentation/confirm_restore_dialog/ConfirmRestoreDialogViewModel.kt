package com.example.gratefulnote.backuprestore.presentation.confirm_restore_dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.backuprestore.data.repository.BackupRestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfirmRestoreDialogViewModel(app : Application) : AndroidViewModel(app){
    private val repository = BackupRestoreRepository(app)

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
            repository.restoreBackupFile(_state.value.restoreFile!!.file)
                .collect { response ->
                    _state.update { it.copy(restoreState = response) }
                }
        }
    }
}

data class ConfirmRestoreDialogState(
    val restoreFile : DocumentFileDto? = null,
    val restoreState : ResponseWrapper<Nothing>? = null
)

sealed class ConfirmRestoreDialogEvent {
    data object ResetState : ConfirmRestoreDialogEvent()
    class InitState(val restoreFile : DocumentFileDto?) : ConfirmRestoreDialogEvent()
    data object RestoreConfirmed : ConfirmRestoreDialogEvent()

}