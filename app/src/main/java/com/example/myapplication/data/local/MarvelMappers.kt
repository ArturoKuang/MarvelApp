package com.example.myapplication.data.local

import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.Thumbnail
import com.example.myapplication.ui.model.MarvelCardData

fun Result.toMarvelEntity(): MarvelEntity {
    return MarvelEntity(
        name = name,
        thumbnailUrl = thumbnail.getImagePath(Thumbnail.ImageSize.STANDARD_AMAZING),
        preference = Preference.NONE
    )
}

fun MarvelEntity.toMarvelCardData(): MarvelCardData {
    return MarvelCardData(
        name, thumbnailUrl, preference
    )
}

fun MarvelCardData.toMarvelEntity(): MarvelEntity {
    return MarvelEntity(
        name, thumbnailUrl, preference
    )
}