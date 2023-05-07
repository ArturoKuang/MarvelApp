package com.example.myapplication.data.remote

import com.example.myapplication.data.model.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET

interface MarvelApi {
    @GET("v1/public/characters")
    suspend fun getCharacters(): Response<CharacterResponse>
}