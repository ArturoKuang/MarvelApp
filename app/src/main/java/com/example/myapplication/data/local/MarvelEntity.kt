package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarvelEntity(
    @PrimaryKey
    val name: String,
    val thumbnailUrl: String,
    val preference: Preference
)


enum class Preference {
    LIKE, DISLIKE, NONE
}