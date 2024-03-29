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
    fun provideDao(@ApplicationContext app : Context) =
        GratefulNoteDatabase.getInstance(app).positiveEmotionDao
}