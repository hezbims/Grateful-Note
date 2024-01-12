package com.example.gratefulnote.backuprestore.presentation.main_screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharedPref : SharedPreferences
    private val contentResolver = app.contentResolver

    init {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPref = app.getSharedPreferences(
                "Backup_Restore_Shared_Preference",
                Context.MODE_PRIVATE
            )

            loadPathLocationFromSharedPref()
        }

    }

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
        val uriString = sharedPref.getString("backup_restore_uri", null)
        _backupRestoreState.update {
            it.copy(pathLocation =
                if (uriString != null) ResponseWrapper.ResponseSucceed(Uri.parse(uriString))
                else ResponseWrapper.ResponseSucceed(null)
            )
        }
        reloadFiles()
    }

    private fun updatePathLocation(newUri: Uri){
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(newUri, takeFlags)

        _backupRestoreState.update {
            it.copy(
                pathLocation = ResponseWrapper.ResponseSucceed(newUri),
                backupFiles = ResponseWrapper.ResponseLoading()
            )
        }

        sharedPref.edit()
            .putString("backup_restore_uri" , newUri.toString())
            .apply()
        reloadFiles()
    }

    private fun reloadFiles(){
        _backupRestoreState.update {
            it.copy(backupFiles = ResponseWrapper.ResponseLoading())
        }


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uri = (_backupRestoreState.value.pathLocation
                        as ResponseWrapper.ResponseSucceed).data

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

class BackupRestoreViewModelFactory(private val app : Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BackupRestoreViewModel::class.java))
            return BackupRestoreViewModel(app) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}