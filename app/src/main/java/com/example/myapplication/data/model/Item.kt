package com.example.myapplication.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("name")
    val name: String,
    @SerialName("resourceURI")
    val resourceURI: String
)