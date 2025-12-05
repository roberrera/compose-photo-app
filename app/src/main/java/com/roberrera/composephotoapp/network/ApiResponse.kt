package com.roberrera.composephotoapp.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sample Photo:
 * {"format":"jpeg","width":5000,"height":3333,"filename":"0.jpeg","id":0,
 * "author":"Alejandro Escamilla","author_url":"https://unsplash.com/photos/yC-Yzbqy7PY",
 * "post_url":"https://unsplash.com/photos/yC-Yzbqy7PY"}
 */

@Serializable
data class Photo(
    @SerialName("id") val id: Int,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("format") val format: String? = "",
    @SerialName("filename") val fileName: String? = "",
    @SerialName("author") val author: String? = "",
    @SerialName("author_url") val authorUrl: String? = "",
    @SerialName("post_url") val postUrl: String? = ""
)