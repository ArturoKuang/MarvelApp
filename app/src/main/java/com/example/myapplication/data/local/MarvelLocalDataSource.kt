package com.example.myapplication.data.local

import com.example.myapplication.data.remote.BaseDataSource
import javax.inject.Inject


class MarvelLocalDataSource @Inject constructor(
    private val marvelDatabase: MarvelDatabase
) : BaseDataSource() {

    suspend fun searchCharacters(name: String): List<MarvelEntity> {
        return marvelDatabase.getMarvelDao().searchCharacters(name)
    }
}