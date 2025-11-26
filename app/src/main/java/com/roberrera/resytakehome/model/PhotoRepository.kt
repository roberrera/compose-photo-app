package com.roberrera.resytakehome.model

import com.roberrera.resytakehome.network.ApiClient
import com.roberrera.resytakehome.network.ApiService
import com.roberrera.resytakehome.network.Photo

class PhotoRepository {

    val apiClient: ApiService = ApiClient.getInstance().create(ApiService::class.java)

    suspend fun fetchPhotos(): List<Photo?>? {
        val request = apiClient.fetchPhotos()
        return if (request.isSuccessful) {
            request.body()?.photos?: emptyList()
        } else {
            null
            // todo: handle error case
        }
    }

}