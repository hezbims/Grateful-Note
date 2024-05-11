package com.example.gratefulnote.hilt_module

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.test.platform.app.InstrumentationRegistry
import com.example.gratefulnote.backuprestore._hilt_module.BackupRestoreModule
import com.example.gratefulnote.backuprestore.data.repository.BackupRestoreManager
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import java.io.File

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [BackupRestoreModule::class]
)
object FakeBackupRestoreModule {
    @Provides
    fun provideBackupRestoreManager() : IBackupRestoreManager{
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        return object : BackupRestoreManager(app = appContext, dao = ){
            override fun persistUriReadAndWritePermission(uri: Uri) {}
            override fun getBackupDirectoryFrom(uri: Uri): DocumentFile {
                return DocumentFile.fromFile(File(uri.path!!))
            }
        }
    }
}