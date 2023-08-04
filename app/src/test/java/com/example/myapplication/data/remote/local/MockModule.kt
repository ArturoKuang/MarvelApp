package com.example.myapplication.data.remote.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.data.local.MarvelDatabase
import com.example.myapplication.data.remote.MarvelApi
import com.example.myapplication.data.remote.MarvelRemoteDataSource
import io.mockk.mockk

object TestModule {

    val marvelDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        MarvelDatabase::class.java
    ).allowMainThreadQueries() // Allow queries on the main thread for testing
        .build()

    val mockMarvelApi = mockk<MarvelApi>()

    // Assuming CharacterResponse is the expected response type from `getCharacters`
    val marvelRemoteDataSource = MarvelRemoteDataSource(mockMarvelApi)
}