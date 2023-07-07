package com.example.myapplication.ui.model

import com.example.myapplication.data.local.Preference

data class MarvelCardData(
    val name: String,
    val thumbnailUrl: String,
    val preference: Preference
)