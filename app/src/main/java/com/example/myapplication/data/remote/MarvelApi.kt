package com.example.myapplication.data.remote

import com.example.myapplication.data.model.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): Response<CharacterResponse>
}