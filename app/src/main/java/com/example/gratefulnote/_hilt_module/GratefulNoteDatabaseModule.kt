package com.example.gratefulnote._hilt_module

import android.content.Context
import com.example.gratefulnote.database.GratefulNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GratefulNoteDatabaseModule {
    @Provides
    @Singleton
    fun provideGratefulNoteDatabase(@ApplicationContext app: Context) =
        GratefulNoteDatabase.getInstance(app)
    @Provides
    @Singleton
    fun providePositiveEmotionDao(database: GratefulNoteDatabase) =
        database.diaryDao
}