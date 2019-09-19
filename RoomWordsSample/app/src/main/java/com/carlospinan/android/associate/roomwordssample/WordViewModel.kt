package com.carlospinan.android.associate.roomwordssample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository = WordRepository(
        WordRoomDatabase.getInstance(application).wordDao(),
        application.applicationContext
    )

    private lateinit var allWords: LiveData<List<Word>>

    init {
        allWords = repository.getAllWords()
    }

    fun getAllWords(): LiveData<List<Word>> = allWords

    fun insert(word: Word) = repository.insert(word)

    fun deleteAll() = repository.deleteAll()

    fun deleteWord(word: Word) = repository.deleteWord(word)
}