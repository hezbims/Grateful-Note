package com.example.gratefulnote.backuprestore.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import com.example.gratefulnote.common.constants.Constants
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.database.Diary
import com.example.gratefulnote.database.DiaryDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.flow

open class BackupRestoreManager (
    private val app : Context,
    private val dao : DiaryDao,
) : IBackupRestoreManager {
    private val contentResolver = app.contentResolver
    override fun loadListOfFilesFrom(uri: Uri) = flow {
        emit(ResponseWrapper.Loading())

        try {
            emit(ResponseWrapper.Succeed(getListOfFilesFrom(uri)))
        } catch (e : Throwable){
            emit(ResponseWrapper.Error(e))
        }
    }

    private fun getListOfFilesFrom(uri : Uri) : List<DocumentFileDto> {
        val backupDirectory = getBackupDirectoryFrom(uri)
        val files = backupDirectory
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

    protected  open fun getBackupDirectoryFrom(uri : Uri) =
        DocumentFile.fromTreeUri(app , uri)!!

    override fun deleteDocumentFile(file: DocumentFile) = flow {
        emit(ResponseWrapper.Loading())
        emit(
            if (file.delete())
                ResponseWrapper.Succeed(Unit)
            else
                ResponseWrapper.Error()
        )
    }

    override fun restoreFile(file : DocumentFile) = flow {
        emit(ResponseWrapper.Loading())

        try {
            val fileContent = contentResolver.openInputStream(file.uri)!!.use { inputStream ->
                inputStream.bufferedReader().use {
                    it.readText()
                }
            }
            val type = object : TypeToken<List<Diary>>() {}.type
            val diaryList = Gson().fromJson(fileContent, type) as List<Diary>

            dao.restoreDiaries(diaryList)
            emit(ResponseWrapper.Succeed(Unit))
        } catch (e : Exception){
            emit(ResponseWrapper.Error(e))
        }
    }

    private val sharedPref = app.getSharedPreferences(
        Constants.SharedPrefs.BackupRestore.name, Context.MODE_PRIVATE,
    )
    override fun getPersistedBackupUri() = flow<ResponseWrapper<Uri?>> {
        emit(ResponseWrapper.Loading())
        val uriString = sharedPref.getString("backup_restore_uri", null)
        if (uriString == null)
            emit(ResponseWrapper.Succeed(null))
        else
            emit(ResponseWrapper.Succeed(Uri.parse(uriString)))
    }

    override suspend fun persistBackupPath(uri : Uri): ResponseWrapper<Unit> {
        return try {
            persistUriReadAndWritePermission(uri)
            sharedPref.edit()
                .putString("backup_restore_uri", uri.toString())
                .apply()
            ResponseWrapper.Succeed(Unit)
        } catch (e : Exception){
            ResponseWrapper.Error(e)
        }
    }

    protected open fun persistUriReadAndWritePermission(uri : Uri){
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)
    }

    override fun createNewBackup(
        backupDirectoryUri: Uri,
        backupTitle: String
    ) = flow {
        emit(ResponseWrapper.Loading())

        var file: DocumentFile? = null
        try {
            if (backupTitle.isEmpty())
                throw Exception("Judul backup tidak boleh kosong")

            val backupFolder = getBackupDirectoryFrom(backupDirectoryUri)

            val backupFileName = "$backupTitle.gn_backup"

            file = backupFolder.createFile(
                "application/json",
                backupFileName
            )

            if ("$backupFileName.json" != file!!.name!!)
                throw Exception("Judul backup ini sudah dipakai")

            contentResolver.openOutputStream(file.uri)!!.use{
                it.write(
                    Gson().toJson(
                        dao.getAllDiaries()
                    ).toByteArray()
                )
            }

            emit(ResponseWrapper.Succeed(Unit))

        } catch (e : Exception){
            file?.delete()
            emit(ResponseWrapper.Error(e))
        }
    }
}