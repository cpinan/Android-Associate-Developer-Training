package com.carlospinan.android.associate.roomwordssample

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordRepository(
    private val wordDao: WordDao,
    private val context: Context
) {

    fun getAllWords(): LiveData<List<Word>> {
        return wordDao.getAllWords()
    }

    fun insert(word: Word) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                wordDao.insert(word)
            }
        }
    }

    fun deleteAll() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                wordDao.deleteAll()
            }
        }
    }

    fun deleteWord(word: Word) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                wordDao.deleteWord(word)
            }
        }
    }

}