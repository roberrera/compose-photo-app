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
            null
            // todo: handle error case
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
            val response: String? = request.body()?.string()
            // The response returns a file URL with a device query after. We don't need the query.
            response?.substringBefore("?")
        } else {
            Log.e("Error", request.errorBody().toString())
            ""
        }
    }

}
