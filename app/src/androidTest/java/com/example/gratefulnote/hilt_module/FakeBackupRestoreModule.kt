package com.example.gratefulnote.hilt_module

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.gratefulnote.backuprestore._hilt_module.BackupRestoreModule
import com.example.gratefulnote.backuprestore.data.repository.BackupRestoreManager
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import com.example.gratefulnote.database.PositiveEmotionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.testing.TestInstallIn
import java.io.File

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [BackupRestoreModule::class]
)
object FakeBackupRestoreModule {
    @Provides
    fun provideBackupRestoreManager(
        @ApplicationContext appContext : Context,
        dao : PositiveEmotionDao,
    ) : IBackupRestoreManager{
        return object : BackupRestoreManager(app = appContext, dao = dao){
            override fun persistUriReadAndWritePermission(uri: Uri) {}
            override fun getBackupDirectoryFrom(uri: Uri): DocumentFile {
                return DocumentFile.fromFile(File(uri.path!!))
            }
        }
    }
}