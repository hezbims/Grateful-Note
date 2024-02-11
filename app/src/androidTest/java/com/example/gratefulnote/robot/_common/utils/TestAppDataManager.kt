package com.example.gratefulnote.robot._common.utils

import android.content.Context
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.common.constants.Constants
import com.example.gratefulnote.database.PositiveEmotionDatabase
import java.io.File

class TestAppDataManager {
    fun clearAppData(){
        val appContext = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        appContext.deleteDatabase(PositiveEmotionDatabase.dbName)
        clearUrisPermissions(appContext)
        clearSharedPreference(Constants.SharedPrefs.Notification.name , appContext)
        clearSharedPreference(Constants.SharedPrefs.BackupRestore.name , appContext)
        clearCacheDirectory(appContext)
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

    private fun clearCacheDirectory(context: Context){
        deleteFile(context.cacheDir)
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