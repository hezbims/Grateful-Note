package com.example.gratefulnote.backuprestore._hilt_module

import android.content.Context
import com.example.gratefulnote.backuprestore.data.repository.BackupRestoreManager
import com.example.gratefulnote.backuprestore.domain.service.IBackupRestoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object BackupRestoreModule {
    @Provides
    fun provideBackupRestoreManager(
        @ApplicationContext context : Context
    ) : IBackupRestoreManager =
        BackupRestoreManager(context)
}