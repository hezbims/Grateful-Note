package com.example.gratefulnote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PositiveEmotion::class] , version = 1 , exportSchema = false)
abstract class PositiveEmotionDatabase : RoomDatabase(){

    abstract val positiveEmotionDatabaseDao : PositiveEmotionDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE : PositiveEmotionDatabase? = null

        fun getInstance(context : Context) : PositiveEmotionDatabase{
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PositiveEmotionDatabase::class.java,
                        "positive_emotion_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}