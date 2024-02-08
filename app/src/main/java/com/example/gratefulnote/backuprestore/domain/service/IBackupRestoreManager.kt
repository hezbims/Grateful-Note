package com.example.gratefulnote.backuprestore.domain.service

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.gratefulnote.backuprestore.domain.model.DocumentFileDto
import com.example.gratefulnote.common.data.dto.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface IBackupRestoreManager {
    fun getListOfFilesFrom(uri : Uri) : Flow<ResponseWrapper<List<DocumentFileDto>>>
    fun deleteDocumentFile(file : DocumentFile) : Flow<ResponseWrapper<Nothing>>
    fun restoreFile(file : DocumentFile) : Flow<ResponseWrapper<Nothing>>

}