package com.example.gratefulnote.hilt_module

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import com.example.gratefulnote._hilt_module.GratefulNoteDatabaseModule
import com.example.gratefulnote.database.GratefulNoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GratefulNoteDatabaseModule::class]
)
object InMemoryDatabaseModule {
    @Provides
    @Singleton
    fun provideGratefulNoteDatabase(@ApplicationContext app: Context) : GratefulNoteDatabase{
        return inMemoryDatabaseBuilder(
            app,
            GratefulNoteDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun providePositiveEmotionDao(database: GratefulNoteDatabase) =
        database.positiveEmotionDao
}