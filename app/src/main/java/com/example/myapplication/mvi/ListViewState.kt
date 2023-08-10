package com.example.myapplication.mvi

import com.example.mvi.ViewState

data class ListViewState(
    val list: List<ListItemViewState>,
    val search: String,
    val refresh: Boolean
): ViewState {
    data class ListItemViewState(
        val name: String,
        val imageUrl: String
    )
}
