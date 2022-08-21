package com.example.gratefulnote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 : Migration by lazy {
    object : Migration(1 , 2){
        override fun migrate(database: SupportSQLiteDatabase) {
            val allPositiveEmotions = mutableListOf<PositiveEmotion>()
            with(database.query("SELECT * FROM positive_emotion_table")){
                while (moveToNext()) {
                    val date = getString(3).split('/').map { it.toInt() }
                    allPositiveEmotions.add(
                        PositiveEmotion(
                            getString(0),
                            getString(1),
                            getString(2),
                            date[0],
                            date[1],
                            date[2],
                            getLong(4)
                        )
                    )
                }
            }
            with(database) {
                execSQL("CREATE TABLE IF NOT EXISTS backup (" +
                        "type TEXT NOT NULL, " +
                        "what TEXT NOT NULL, " +
                        "why TEXT NOT NULL, " +
                        "day INTEGER NOT NULL, " +
                        "month INTEGER NOT NULL, " +
                        "year INTEGER NOT NULL, " +
                        "id INTEGER PRIMARY KEY NOT NULL" +
                        ")"
                )

                for (e in allPositiveEmotions)
                    execSQL(
                        """
                            INSERT INTO backup
                            VALUES (
                                '${e.type}',
                                '${e.what.replace("'" , "''")}',
                                '${e.why.replace("'" , "''")}',
                                ${e.day},
                                ${e.month},
                                ${e.year},
                                ${e.id}
                            )
                        """.trimIndent()
                    )

                execSQL("DROP TABLE positive_emotion_table")
                execSQL("ALTER TABLE backup RENAME TO positive_emotion_table")
            }
        }
    }
}

@Database(entities = [PositiveEmotion::class] , version = 2)
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
                        .addMigrations(MIGRATION_1_2)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}