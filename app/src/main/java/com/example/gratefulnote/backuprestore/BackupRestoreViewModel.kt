package com.example.gratefulnote.backuprestore

import android.app.Application
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.*
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class BackupRestoreViewModel(app : Application) : AndroidViewModel(app) {
    private val dao = PositiveEmotionDatabase
        .getInstance(app)
        .positiveEmotionDatabaseDao

    private fun getBackupFile() : File{
        val path = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ) ,
            getString(R.string.gratefulNote)
        )
        path.mkdir()

        return File(path , getString(R.string.backup_file_name))
    }

    private val backupFile = MutableLiveData(getBackupFile())

    val isBackupFileExists = Transformations.map(backupFile){
        it.exists()
    }

    val lastTimeBackup = Transformations.map(backupFile){
        getString(
            R.string.last_time_backup ,
            if (it.exists()){
                val formatter = SimpleDateFormat("d MMMM yyyy, HH:mm" , Locale.getDefault())
                formatter.format(Date(it.lastModified()))
            }
            else getString(R.string.data_kosong)
        )
    }

    val fileSize = Transformations.map(backupFile){
        getString(
            R.string.file_size,
            if (it.exists()) String.format("%.2f KB" , it.length().toDouble() / 1024)
            else getString(R.string.data_kosong)
        )
    }

    private var _isBackuping = false

    fun backup(){
        if(!_isBackuping) {
            _isBackuping = true
            viewModelScope.launch(Dispatchers.IO) {
                val data = dao.getAllPositiveEmotion(
                    0,
                    null,
                    getString(R.string.semua)
                )

                FileOutputStream(getBackupFile()).use {
                    it.write(Gson().toJson(data).toByteArray())
                }

                _isBackuping = false
                withContext(Dispatchers.Main){
                    backupFile.value = getBackupFile()
                }
            }
        }
    }

    fun restore(){
        if (backupFile.value!!.exists()) {
            val jsonString = FileInputStream(backupFile.value).bufferedReader().use {
                it.readText()
            }

            val type = object : TypeToken<List<PositiveEmotion>>() {}.type
            val positiveEmotions = Gson().fromJson(jsonString, type) as List<PositiveEmotion>

            viewModelScope.launch(Dispatchers.IO) {
                dao.deleteAll()
                dao.insertAll(positiveEmotions)
            }
        }
        else
            Toast.makeText(
                getApplication() ,
                "Backup file gak ada" ,
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun getString(id : Int , vararg arg : String) =
        getApplication<Application>().getString(id , *arg)
}

class BackupRestoreViewModelFactory(private val app : Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BackupRestoreViewModel::class.java))
            return BackupRestoreViewModel(app) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}