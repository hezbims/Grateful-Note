package com.example.gratefulnote.backuprestore

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.*
import com.example.gratefulnote.R
import com.example.gratefulnote.common.data.ResponseWrapper
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackupRestoreViewModel(private val app : Application) : AndroidViewModel(app) {

    private val dao = PositiveEmotionDatabase
        .getInstance(app)
        .positiveEmotionDatabaseDao

    private val _isProcessing = MutableLiveData(ProcessingStatus.NONE)

    private val _backupRestoreState = MutableStateFlow(BackupRestoreViewState())
    val backupRestoreState : StateFlow<BackupRestoreViewState>
        get() = _backupRestoreState

    fun onEvent(event: BackupRestoreStateEvent){
        when(event){
            is BackupRestoreStateEvent.EventUpdatePathLocation -> {
                _backupRestoreState.update {
                    it.copy(
                        pathLocation = event.newUri,
                        backupFiles = ResponseWrapper.ResponseLoading()
                    )
                }
                loadFiles()
            }
        }
    }

    fun loadFiles(){
        _backupRestoreState.update {
            it.copy(backupFiles = ResponseWrapper.ResponseLoading())
        }
        val uri = _backupRestoreState.value.pathLocation

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val documentTree = DocumentFile.fromTreeUri(app , uri!!)!!
                val files = documentTree.listFiles().toList()
                _backupRestoreState.update {
                    it.copy(backupFiles = ResponseWrapper.ResponseLoaded(files))
                }
            } catch (e : Exception){
                _backupRestoreState.update {
                    it.copy(backupFiles = ResponseWrapper.ResponseError(e))
                }
            }
        }
    }



    fun backup(uri : Uri){
        _isProcessing.value = ProcessingStatus.BACKUP
        viewModelScope.launch(Dispatchers.IO) {
            val data = dao.getAllPositiveEmotion(
                0,
                null,
                app.getString(R.string.semua),
                false
            )

            app.contentResolver.openOutputStream(uri)!!.use {
                it.write(Gson().toJson(data).toByteArray())
            }


            withContext(Dispatchers.Main){
                _isProcessing.value = ProcessingStatus.NONE
            }
        }
    }

    fun restore(uri : Uri){
        if (_isProcessing.value!! == ProcessingStatus.NONE) {
            _isProcessing.value = ProcessingStatus.RESTORE
            val jsonString = app.contentResolver.openInputStream(uri)!!.use {inputStream ->
                inputStream.bufferedReader().use {
                    it.readText()
                }
            }

            val type = object : TypeToken<List<PositiveEmotion>>() {}.type
            val positiveEmotions = Gson().fromJson(jsonString, type) as List<PositiveEmotion>

            viewModelScope.launch(Dispatchers.IO) {
                dao.deleteAll()
                dao.insertAll(positiveEmotions)
                withContext(Dispatchers.Main) {
                    _isProcessing.value = ProcessingStatus.NONE
                }
            }
        }
    }

    private companion object{
        enum class ProcessingStatus{
            BACKUP , RESTORE , NONE
        }
    }
}

data class BackupRestoreViewState(
    val pathLocation : Uri? = null,
    val backupFiles : ResponseWrapper? = null,
)

sealed class BackupRestoreStateEvent {
    class EventUpdatePathLocation(val newUri: Uri) : BackupRestoreStateEvent()
}

class BackupRestoreViewModelFactory(private val app : Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BackupRestoreViewModel::class.java))
            return BackupRestoreViewModel(app) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}