package com.example.gratefulnote.backuprestore.viewmodel

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gratefulnote.common.data.ResponseWrapper
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewBackupDialogViewModel(private val app : Application) : AndroidViewModel(app) {
    private val dao = PositiveEmotionDatabase.getInstance(app).positiveEmotionDatabaseDao

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
        _state.update {
            it.copy(createNewBackupStatus = ResponseWrapper.ResponseLoading())
        }

        var file: DocumentFile? = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val folder = DocumentFile.fromTreeUri(app, _targetDocumentTree!!)!!
                file = folder.createFile(
                    "application/json",
                    "${_state.value.backupTitle}.gn_backup.json"
                )
                val fileNameSegments = file!!.name!!.split('.')
                if (fileNameSegments.first().isEmpty())
                    throw Exception("Judul backup tidak boleh kosong")
                if (fileNameSegments[1] != "gn_backup")
                    throw Exception("Judul backup ini sudah dipakai")

                app.contentResolver.openOutputStream(file!!.uri)!!.use{
                    it.write(
                        Gson().toJson(
                            dao.getAllPositiveEmotion()
                        ).toByteArray()
                    )
                }


                _state.update {
                    it.copy(createNewBackupStatus = ResponseWrapper.ResponseSucceed<Nothing>())
                }

            } catch (e : Exception){
                file?.delete()
                _state.update {
                    it.copy(createNewBackupStatus = ResponseWrapper.ResponseError(e))
                }
            }
        }
    }
}

data class CreateNewBackupDialogState(
    val createNewBackupStatus : ResponseWrapper? = null,
    val backupTitle : String = "",
)

sealed class CreateNewBackupDialogEvent {
    class OnChangeBackupTitle(val newTitle : String) : CreateNewBackupDialogEvent()
    data object OnResetViewModelState : CreateNewBackupDialogEvent()
    class OnInitDocumentTreeUri(val uri : Uri) : CreateNewBackupDialogEvent()
    data object OnCreateNewBackup : CreateNewBackupDialogEvent()
}