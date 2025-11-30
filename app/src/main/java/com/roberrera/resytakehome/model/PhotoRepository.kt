package com.roberrera.resytakehome.model

import android.util.Log
import com.roberrera.resytakehome.network.ApiClient
import com.roberrera.resytakehome.network.ApiService
import com.roberrera.resytakehome.network.Photo
import javax.inject.Inject

class PhotoRepository @Inject constructor() {

    private val apiClient: ApiService = ApiClient.getInstance().create(ApiService::class.java)

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
            // The response gives us an image URL, so we can just return the raw URL string and
            // let Coil process it to display it as an image.
            request.raw().request.url.toString()
        } else {
            Log.e("Error", request.errorBody().toString())
            null
        }
    }

}
