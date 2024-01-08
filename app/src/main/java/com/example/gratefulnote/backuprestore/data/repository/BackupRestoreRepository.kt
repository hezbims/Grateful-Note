package com.example.gratefulnote.backuprestore.data.repository

import android.app.Application
import androidx.documentfile.provider.DocumentFile
import androidx.room.withTransaction
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.flow

class BackupRestoreRepository(app : Application) {
    private val database = PositiveEmotionDatabase.getInstance(app)
    private val dao = database.positiveEmotionDatabaseDao
    private val contentResolver = app.contentResolver
    fun restoreBackupFile(file : DocumentFile) = flow<ResponseWrapper<Nothing>> {
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