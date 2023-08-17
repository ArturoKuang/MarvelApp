package com.example.myapplication.ui

import com.example.myapplication.ui.model.MarvelDisplayData


data class MarvelStateMvm(
    val list: List<MarvelDisplayData>,
    val loading: Boolean,
    val query: String = ""
) {
    val progress: Float = if (loading) 0f else 100f
}