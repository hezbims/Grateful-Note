package com.example.gratefulnote._hilt_module

import android.content.Context
import com.example.gratefulnote.database.GratefulNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GratefulNoteAppModule {
    @Provides
    fun provideGratefulNoteDatabase(@ApplicationContext app: Context) =
        GratefulNoteDatabase.getInstance(app)
    @Provides
    fun provideDao(database: GratefulNoteDatabase) =
        database.positiveEmotionDao
}