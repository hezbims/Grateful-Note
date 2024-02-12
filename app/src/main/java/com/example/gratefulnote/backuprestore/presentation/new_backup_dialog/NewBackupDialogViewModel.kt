package com.example.gratefulnote.backuprestore.presentation.new_backup_dialog

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBackupDialogViewModel @Inject constructor(
    private val backupRestoreManager: IBackupRestoreManager
) : ViewModel() {
    private val _state = MutableStateFlow(CreateNewBackupDialogState())
    private var _targetDocumentTree : Uri? = null

    val state : StateFlow<CreateNewBackupDialogState>
        get() = _state

    fun onEvent(event: CreateNewBackupDialogEvent){
        when (event){
            is CreateNewBackupDialogEvent.OnChangeBackupTitle ->
                _state.update {
                    it.copy(backupTitle = event.newTitle)
                }
            CreateNewBackupDialogEvent.OnResetViewModelState ->
                _state.update {
                    CreateNewBackupDialogState()
                }
            is CreateNewBackupDialogEvent.OnInitDocumentTreeUri ->
                _targetDocumentTree = event.uri
            CreateNewBackupDialogEvent.OnCreateNewBackup ->
                createNewBackup()
        }
    }

    private fun createNewBackup(){
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreManager.createNewBackup(
                backupDirectoryUri = _targetDocumentTree!!,
                backupTitle = _state.value.backupTitle,
            ).collect{response ->
                _state.update {
                    it.copy(createNewBackupStatus = response)
                }
            }
        }
    }
}

data class CreateNewBackupDialogState(
    val createNewBackupStatus : ResponseWrapper<Nothing>? = null,
    val backupTitle : String = "",
)

sealed class CreateNewBackupDialogEvent {
    class OnChangeBackupTitle(val newTitle : String) : CreateNewBackupDialogEvent()
    data object OnResetViewModelState : CreateNewBackupDialogEvent()
    class OnInitDocumentTreeUri(val uri : Uri) : CreateNewBackupDialogEvent()
    data object OnCreateNewBackup : CreateNewBackupDialogEvent()
}