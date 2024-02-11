package com.example.gratefulnote.backuprestore.presentation.main_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
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
class MainScreenViewModel @Inject constructor(
    private val backupRestoreManager: IBackupRestoreManager
) : ViewModel() {
    private val _backupRestoreState = MutableStateFlow(BackupRestoreViewState())
    val backupRestoreState : StateFlow<BackupRestoreViewState>
        get() = _backupRestoreState

    init { loadPathLocationFromSharedPref() }

    fun onEvent(event: BackupRestoreStateEvent){
        when(event){
            BackupRestoreStateEvent.LoadPathFromSharedPref ->
                loadPathLocationFromSharedPref()
            is BackupRestoreStateEvent.UpdatePathLocation ->
                updatePathLocation(event.newUri)
            BackupRestoreStateEvent.OpenNewBackupDialog ->
                _backupRestoreState.update {
                    it.copy(openCreateNewBackupDialog = true)
                }
            is BackupRestoreStateEvent.RequestDismissNewBackupDialog -> {
                // enggak bisa ngedismiss dialog pas lagi proses loading
                if (event.dialogStatus !is ResponseWrapper.ResponseLoading<*>) {
                    _backupRestoreState.update {
                        it.copy(openCreateNewBackupDialog = false)
                    }
                    // Kalo berhasil, load files ulang agar menampilkan hasil bakup terbaru juga
                    if (event.dialogStatus is ResponseWrapper.ResponseSucceed<*>)
                        reloadFiles()
                }
            }
            BackupRestoreStateEvent.ReloadBackupFileList ->
                reloadFiles()
            is BackupRestoreStateEvent.DeleteFile ->
                deleteFile(event.file)
            is BackupRestoreStateEvent.OpenRestoreConfirmationDialog ->
                _backupRestoreState.update { it.copy(restoreFile = event.file) }
            is BackupRestoreStateEvent.RequestDismissRestoreConfirmationDialog ->
                if (event.dialogStatus !is ResponseWrapper.ResponseLoading)
                    _backupRestoreState.update { it.copy(restoreFile = null) }

        }
    }

    private fun loadPathLocationFromSharedPref(){
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreManager.getPersistedBackupUri().collect { response ->
                _backupRestoreState.update { it.copy(pathLocation = response) }
                if (response is ResponseWrapper.ResponseSucceed && response.data != null)
                    reloadFiles()
            }

        }
    }

    private fun updatePathLocation(newUri: Uri){
        _backupRestoreState.update {
            it.copy(
                pathLocation = ResponseWrapper.ResponseSucceed(newUri),
                backupFiles = ResponseWrapper.ResponseLoading()
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreManager.persistBackupPath(newUri)
            reloadFiles()
        }
    }

    private fun reloadFiles(){
        viewModelScope.launch(Dispatchers.IO) {
            val uri =
                (_backupRestoreState.value.pathLocation as ResponseWrapper.ResponseSucceed).data!!
            backupRestoreManager.loadListOfFilesFrom(uri).collect { result ->
                _backupRestoreState.update {
                    it.copy(backupFiles = result)
                }
            }
        }
    }

    private fun deleteFile(deletedFile: DocumentFileDto){
        viewModelScope.launch(Dispatchers.IO) {
            backupRestoreManager.deleteDocumentFile(deletedFile.file).collect {result ->
                if (result is ResponseWrapper.ResponseSucceed)
                    reloadFiles()
            }
        }
    }
}

data class BackupRestoreViewState(
    val pathLocation : ResponseWrapper<Uri> = ResponseWrapper.ResponseLoading(),
    val backupFiles : ResponseWrapper<List<DocumentFileDto>>? = null,
    val openCreateNewBackupDialog : Boolean = false,
    val restoreFile : DocumentFileDto? = null,
)

sealed class BackupRestoreStateEvent {
    data object LoadPathFromSharedPref : BackupRestoreStateEvent()
    class UpdatePathLocation(val newUri: Uri) : BackupRestoreStateEvent()
    data object OpenNewBackupDialog : BackupRestoreStateEvent()
    class RequestDismissNewBackupDialog(val dialogStatus : ResponseWrapper<Nothing>?) : BackupRestoreStateEvent()
    data object ReloadBackupFileList : BackupRestoreStateEvent()
    class DeleteFile(val file : DocumentFileDto) : BackupRestoreStateEvent()
    class OpenRestoreConfirmationDialog(val file : DocumentFileDto) : BackupRestoreStateEvent()
    class RequestDismissRestoreConfirmationDialog(val dialogStatus : ResponseWrapper<Nothing>?) : BackupRestoreStateEvent()
}