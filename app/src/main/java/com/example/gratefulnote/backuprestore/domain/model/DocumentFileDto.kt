package com.example.gratefulnote.backuprestore.domain.model

import androidx.documentfile.provider.DocumentFile
import java.text.SimpleDateFormat

data class DocumentFileDto (
    val fileName : String,
    val lastModified : String,
    val file : DocumentFile
){
    companion object {
        private val dateFormatter = SimpleDateFormat("dd-LLL-yyyy, HH:mm")
        fun from(documentFile: DocumentFile) : DocumentFileDto {
            val nameSegments = documentFile.name!!.split('.')
            return DocumentFileDto(
                fileName = nameSegments.first(),
                lastModified = dateFormatter.format(documentFile.lastModified()),
                file = documentFile
            )
        }
    }
}