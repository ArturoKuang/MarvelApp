package com.example.myapplication.data.remote

import retrofit2.http.Query
import javax.inject.Inject

class MarvelRemoteDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : BaseDataSource() {

    suspend fun getCharacters(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ) = getResult {
        marvelApi.getCharacters(limit, offset)
    }
}