package com.roberrera.resytakehome.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ApiResponse(
    @SerialName("photos") val photos: List<Photo?>?
)

@Serializable
data class Photo(
    val id: String,
    val width: Int,
    val height: Int,
    val imageFileName: String?
)