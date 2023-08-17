package com.example.myapplication.ui.model

import com.example.myapplication.data.local.MarvelEntity

data class MarvelDisplayData(
    val name: String,
    val imageUrl: String
)

fun MarvelEntity.asEntity(): MarvelDisplayData =
    MarvelDisplayData(name, thumbnailUrl)