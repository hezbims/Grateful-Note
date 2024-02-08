package com.example.gratefulnote.backuprestore.data.repository

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.room.withTransaction
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BackupRestoreManager(app : Context) : IBackupRestoreManager {
    private val database = PositiveEmotionDatabase.getInstance(app)
    private val dao = database.positiveEmotionDatabaseDao
    private val contentResolver = app.contentResolver
    override fun getListOfFilesFrom(uri: Uri): Flow<ResponseWrapper<List<DocumentFileDto>>> {
        throw Exception()
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
}