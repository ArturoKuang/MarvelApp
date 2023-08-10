package com.example.myapplication.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    @SerialName("attributionHTML")
    val attributionHTML: String,
    @SerialName("attributionText")
    val attributionText: String,
    @SerialName("code")
    val code: Int,
    @SerialName("copyright")
    val copyright: String,
    @SerialName("data")
    val marvelData: Data,
    @SerialName("etag")
    val etag: String,
    @SerialName("status")
    val status: String
)