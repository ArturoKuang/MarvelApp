package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarvelEntity::class], version = 1, exportSchema = false)
abstract class MarvelDatabase : RoomDatabase() {
    abstract val marvelDao: MarvelDao
}