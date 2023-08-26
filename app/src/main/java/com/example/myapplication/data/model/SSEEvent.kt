package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SSEEventData(
    val status: STATUS? = null,
    val image: String? = null
)

enum class STATUS {
    SUCCESS,
    ERROR,
    NONE,
    CLOSED,
    OPEN
}