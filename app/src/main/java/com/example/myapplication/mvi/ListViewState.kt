package com.example.myapplication.mvi

import com.example.mvi.ViewState
import com.example.myapplication.data.model.CharacterResponse
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.Thumbnail

data class ListViewState(
    val list: List<ListItemViewState> = emptyList(),
    val search: String = "",
    val refresh: Boolean = false,
    val popUpMessage: String = "",
) : ViewState {
    data class ListItemViewState(
        val name: String, val imageUrl: String
    )
}


fun CharacterResponse.asEntity(): List<ListViewState.ListItemViewState> {
    return marvelData.results.map { result -> result.asEntity() }
}

fun Result.asEntity() = ListViewState.ListItemViewState(
    name = name, imageUrl = thumbnail.getImagePath(Thumbnail.ImageSize.STANDARD_AMAZING)
)
