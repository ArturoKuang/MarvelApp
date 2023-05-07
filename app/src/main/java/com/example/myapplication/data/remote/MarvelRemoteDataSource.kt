package com.example.myapplication.data.remote

import javax.inject.Inject

class MarvelRemoteDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : BaseDataSource() {

    suspend fun getCharacters() = getResult {
        marvelApi.getCharacters()
    }
}