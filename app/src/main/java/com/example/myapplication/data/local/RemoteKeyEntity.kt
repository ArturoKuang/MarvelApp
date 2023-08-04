package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val name: String,
    val prevKey: Int?,
    val nextKey: Int?
)