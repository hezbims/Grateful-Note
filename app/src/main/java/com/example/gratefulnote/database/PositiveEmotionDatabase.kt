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
                            type = getString(0),
                            what = getString(1),
                            why = getString(2),
                            day = date[0],
                            month = date[1],
                            year = date[2],
                            id = getLong(4)
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

val MIGRATION_2_3 = object : Migration(2 , 3){
    override fun migrate(database: SupportSQLiteDatabase) {
        val allPositiveEmotions = mutableListOf<PositiveEmotion>()
        with(database.query("SELECT * FROM positive_emotion_table")){
            while(moveToNext())
                allPositiveEmotions.add(
                    PositiveEmotion(
                        type = getString(0),
                        what = getString(1),
                        why = getString(2),
                        day = getInt(3),
                        month = getInt(4),
                        year = getInt(5),
                        id = getLong(6)
                    )
                )
        }

        with(database){
            execSQL("""CREATE TABLE IF NOT EXISTS backup (
                type TEXT NOT NULL, 
                what TEXT NOT NULL, 
                why TEXT NOT NULL,
                day INTEGER NOT NULL,
                month INTEGER NOT NULL,
                year INTEGER NOT NULL,
                isFavorite INTEGER NOT NULL,
                id INTEGER PRIMARY KEY NOT NULL
                )
            """.trimIndent())

            for (p in allPositiveEmotions)
                execSQL("""
                    INSERT INTO backup
                    VALUES (
                    '${p.type}',
                    '${p.what.replace("'" , "''")}', 
                    '${p.why.replace("'" , "''")}',
                    '${p.day}',
                    '${p.month}',
                    '${p.year}',
                    '${p.isFavorite}',
                    '${p.id}'                                        
                    )""".trimIndent())

            execSQL("DROP TABLE positive_emotion_table")
            execSQL("ALTER TABLE backup RENAME TO positive_emotion_table")
        }
    }
}

@Database(entities = [PositiveEmotion::class] , version = 3)
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
                        .addMigrations(MIGRATION_2_3)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}