package com.roberrera.resytakehome.model

import android.util.Log
import com.roberrera.resytakehome.network.ApiService
import com.roberrera.resytakehome.network.Photo
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val apiClient: ApiService) {

    suspend fun fetchPhotos(): List<Photo?>? {
        val request = apiClient.fetchPhotos()
        return if (request.isSuccessful) {
            request.body()?: emptyList()
        } else {
            Log.e("Error", request.errorBody().toString())
            null
        }
    }

    suspend fun fetchPhotoById(
        width: Int,
        height: Int,
        id: Int
    ): String? {
        val request = apiClient.fetchPhotoById(
            width = width,
            height = height,
            id = id
        )
        return if (request.isSuccessful) {
            // Instead of a Photo object, the response gives us a raw image URL,
            // which our image loader can handle.
            request.raw().request.url.toString()
        } else {
            Log.e("Error", request.errorBody().toString())
            ""
        }
    }

}
