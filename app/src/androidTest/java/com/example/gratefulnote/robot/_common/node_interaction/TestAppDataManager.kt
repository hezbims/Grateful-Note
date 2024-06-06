package com.example.gratefulnote.robot._common.node_interaction

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.example.gratefulnote.common.constants.Constants
import com.example.gratefulnote.database.GratefulNoteDatabase
import java.io.File

class TestAppDataManager {
    fun clearAppData(){
        val appContext = ApplicationProvider.getApplicationContext<Context>()

        appContext.deleteDatabase(GratefulNoteDatabase.dbName)
        clearUrisPermissions(appContext)
        clearSharedPreference(Constants.SharedPrefs.Notification.name , appContext)
        clearSharedPreference(Constants.SharedPrefs.BackupRestore.name , appContext)
        clearFilesDirectory(appContext)
    }

    private fun clearUrisPermissions(context: Context){
        val contentResolver = context.contentResolver
        val persistedUriPermissions = contentResolver.persistedUriPermissions
        for (permission in persistedUriPermissions) {
            try {
                contentResolver.releasePersistableUriPermission(
                    permission.uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    private fun clearSharedPreference(name : String , context : Context){
        context.getSharedPreferences(name , Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    private fun clearFilesDirectory(context: Context){
        deleteFile(context.filesDir)
    }

    private fun deleteFile(file : File){
        if (file.isDirectory){
            for (childFile in file.listFiles()!!){
                deleteFile(childFile)
            }
        }
        file.delete()
    }
}