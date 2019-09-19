package com.carlospinan.android.associate.roomwordssample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [Word::class],
    version = 1,
    exportSchema = false
)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: WordRoomDatabase? = null

        var words = arrayOf("dolphin", "crocodile", "cobra")

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        instance?.let {
                            val wordDao = it.wordDao()
                            // wordDao.deleteAll()
                            if (wordDao.getAnyWord().isEmpty()) {
                                for (word in words) {
                                    wordDao.insert(Word(word = word))
                                }
                            }
                        }
                    }
                }
            }
        }

        fun getInstance(context: Context): WordRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): WordRoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WordRoomDatabase::class.java,
                "word_database"
            )
                .fallbackToDestructiveMigration()
                .addCallback(sRoomDatabaseCallback)
                .build()
        }
    }
}