package com.example.gratefulnote.backuprestore.domain.service

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.common.domain.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface IBackupRestoreManager {
    fun loadListOfFilesFrom(uri : Uri) : Flow<ResponseWrapper<List<DocumentFileDto>>>
    fun deleteDocumentFile(file : DocumentFile) : Flow<ResponseWrapper<Unit>>
    fun restoreFile(file : DocumentFile) : Flow<ResponseWrapper<Unit>>
    fun getPersistedBackupUri() : Flow<ResponseWrapper<Uri?>>
    suspend fun persistBackupPath(uri : Uri) : ResponseWrapper<Unit>
    fun createNewBackup(
        backupDirectoryUri : Uri,
        backupTitle : String
    ) : Flow<ResponseWrapper<Unit>>

}