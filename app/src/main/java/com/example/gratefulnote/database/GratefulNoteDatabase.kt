package com.example.gratefulnote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 : Migration by lazy {
    object : Migration(1 , 2){
        override fun migrate(db: SupportSQLiteDatabase) {
            val allPositiveEmotions = mutableListOf<PositiveEmotion>()
            with(db.query("SELECT * FROM positive_emotion_table")){
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
            with(db) {
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
    override fun migrate(db: SupportSQLiteDatabase) {
        val allPositiveEmotions = mutableListOf<PositiveEmotion>()
        with(db.query("SELECT * FROM positive_emotion_table")){
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

        with(db){
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

@Database(
    entities = [PositiveEmotion::class , DailyNotificationEntity::class] ,
    version = 4
)
abstract class GratefulNoteDatabase : RoomDatabase(){

    abstract val positiveEmotionDao : PositiveEmotionDao
    abstract val dailyNotificationDao : DailyNotificationDao

    companion object {
        const val dbName = "positive_emotion_database"
        @Volatile
        private var INSTANCE : GratefulNoteDatabase? = null

        fun getInstance(context : Context) : GratefulNoteDatabase{
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GratefulNoteDatabase::class.java,
                        dbName
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