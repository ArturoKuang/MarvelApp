package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RemoteKeyEntity::class], version = 1, exportSchema = false)
abstract class RemoteKeyDatabase : RoomDatabase() {
    abstract val remoteKeyDao: RemoteKeyDao
}