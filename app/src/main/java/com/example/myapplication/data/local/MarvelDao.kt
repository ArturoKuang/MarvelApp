package com.example.myapplication.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MarvelDao {
    @Upsert
    suspend fun insertAll(data: List<MarvelEntity>)

    @Upsert
    suspend fun insert(data: MarvelEntity)

    @Query("SELECT * FROM MarvelEntity WHERE name LIKE '%' || :namePart || '%'")
    suspend fun searchCharacters(namePart: String): List<MarvelEntity>

    @Query("DELETE FROM marvelentity")
    suspend fun clearAll()

    @Query("SELECT * FROM MarvelEntity")
    fun pagingSource(): PagingSource<Int, MarvelEntity>
}