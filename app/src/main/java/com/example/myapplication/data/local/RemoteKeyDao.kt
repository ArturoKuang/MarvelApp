package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun insertAll(data: List<RemoteKeyEntity>)

    @Upsert
    suspend fun insert(data: RemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE name = :name")
    suspend fun remoteKeysById(name: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}