package com.example.gratefulnote.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PositiveEmotion::class] , version = 1 , exportSchema = false)
abstract class Room : RoomDatabase(){

    abstract val dao : Dao

    companion object {
        @Volatile
        private var INSTANCE : Room? = null

        fun getInstance(context : Context) : Room{
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        Room::class.java,
                        "sleep_history_database"
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