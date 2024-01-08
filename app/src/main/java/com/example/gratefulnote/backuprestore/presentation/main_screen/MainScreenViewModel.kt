package com.example.gratefulnote.backuprestore.presentation.main_screen

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.*
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BackupRestoreViewModel(private val app : Application) : AndroidViewModel(app) {
    private val _backupRestoreState = MutableStateFlow(BackupRestoreViewState())
    val backupRestoreState : StateFlow<BackupRestoreViewState>
        get() = _backupRestoreState

    fun onEvent(event: BackupRestoreStateEvent){
        when(event){
            is BackupRestoreStateEvent.UpdatePathLocation -> {
                _backupRestoreState.update {
                    it.copy(
                        pathLocation = event.newUri,
                        backupFiles = ResponseWrapper.ResponseLoading()
                    )
                }
                reloadFiles()
            }
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

    private fun reloadFiles(){
        _backupRestoreState.update {
            it.copy(backupFiles = ResponseWrapper.ResponseLoading())
        }
        val uri = _backupRestoreState.value.pathLocation

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val documentTree = DocumentFile.fromTreeUri(app , uri!!)!!
                val files = documentTree
                    .listFiles()
                    .toList()
                    .filter {file ->
                        val nameSegments = file.name!!.split('.')

                        nameSegments.size >= 3 &&
                            nameSegments.last() == "json" &&
                            nameSegments[nameSegments.lastIndex - 1].contains("gn_backup")
                    }
                    .sortedBy { file -> -file.lastModified() }
                    .map { file -> DocumentFileDto.from(file) }
                _backupRestoreState.update {
                    it.copy(backupFiles = ResponseWrapper.ResponseSucceed(files))
                }
            } catch (e : Exception){
                _backupRestoreState.update {
                    it.copy(backupFiles = ResponseWrapper.ResponseError(e))
                }
            }
        }
    }

    private fun deleteFile(deletedFile: DocumentFileDto){
        val deleteSucceed = deletedFile.file.delete()
        if (deleteSucceed)
            reloadFiles()
    }
}

data class BackupRestoreViewState(
    val pathLocation : Uri? = null,
    val backupFiles : ResponseWrapper<List<DocumentFileDto>>? = null,
    val openCreateNewBackupDialog : Boolean = false,
    val restoreFile : DocumentFileDto? = null,
)

sealed class BackupRestoreStateEvent {
    class UpdatePathLocation(val newUri: Uri) : BackupRestoreStateEvent()
    data object OpenNewBackupDialog : BackupRestoreStateEvent()
    class RequestDismissNewBackupDialog(val dialogStatus : ResponseWrapper<Nothing>?) : BackupRestoreStateEvent()
    data object ReloadBackupFileList : BackupRestoreStateEvent()
    class DeleteFile(val file : DocumentFileDto) : BackupRestoreStateEvent()
    class OpenRestoreConfirmationDialog(val file : DocumentFileDto) : BackupRestoreStateEvent()
    class RequestDismissRestoreConfirmationDialog(val dialogStatus : ResponseWrapper<Nothing>?) : BackupRestoreStateEvent()
}

class BackupRestoreViewModelFactory(private val app : Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BackupRestoreViewModel::class.java))
            return BackupRestoreViewModel(app) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}