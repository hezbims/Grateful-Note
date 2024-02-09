package com.example.gratefulnote.backuprestore.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.room.withTransaction
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import com.example.gratefulnote.common.constants.Constants
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BackupRestoreManager (
    private val app : Context
) : IBackupRestoreManager {
    private val database = PositiveEmotionDatabase.getInstance(app)
    private val dao = database.positiveEmotionDatabaseDao
    private val contentResolver = app.contentResolver
    override fun loadListOfFilesFrom(uri: Uri) = flow {
        emit(ResponseWrapper.ResponseLoading())

        try {
            emit(ResponseWrapper.ResponseSucceed(getListOfFilesFrom(uri)))
        } catch (e : Throwable){
            emit(ResponseWrapper.ResponseError(e))
        }
    }

    private fun getListOfFilesFrom(uri : Uri) : List<DocumentFileDto> {
        val documentTree = DocumentFile.fromTreeUri(app , uri)!!
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
        return files
    }

    override fun deleteDocumentFile(file: DocumentFile): Flow<ResponseWrapper<Nothing>> {
        throw Exception()
    }

    override fun restoreFile(file : DocumentFile) = flow<ResponseWrapper<Nothing>> {
        emit(ResponseWrapper.ResponseLoading())

        try {
            val fileContent = contentResolver.openInputStream(file.uri)!!.use { inputStream ->
                inputStream.bufferedReader().use {
                    it.readText()
                }
            }
            val type = object : TypeToken<List<PositiveEmotion>>() {}.type
            val positiveEmotionList = Gson().fromJson(fileContent, type) as List<PositiveEmotion>

            database.withTransaction {
                dao.deleteAll()
                dao.insertAll(positiveEmotionList)
            }
            emit(ResponseWrapper.ResponseSucceed())
        } catch (e : Exception){
            emit(ResponseWrapper.ResponseError(e))
        }
    }

    private val sharedPref = app.getSharedPreferences(
        Constants.SharedPrefs.BackupRestore.name, Context.MODE_PRIVATE,
    )
    override fun getPersistedBackupUri() = flow<ResponseWrapper<Uri>> {
        emit(ResponseWrapper.ResponseLoading())
        val uriString = sharedPref.getString("backup_restore_uri", null)
        if (uriString == null)
            emit(ResponseWrapper.ResponseSucceed(null))
        else
            emit(ResponseWrapper.ResponseSucceed(Uri.parse(uriString)))
    }

    override suspend fun persistPath(uri : Uri): ResponseWrapper<Nothing> {
        try {
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, takeFlags)

            sharedPref.edit()
                .putString("backup_restore_uri", uri.toString())
                .apply()
            return ResponseWrapper.ResponseSucceed()
        } catch (e : Exception){
            return ResponseWrapper.ResponseError(e)
        }
    }
}