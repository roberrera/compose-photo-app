package com.roberrera.resytakehome.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API service interface for Retrofit.
 * This defines the endpoints for fetching Photo data.
 */
interface ApiService {
    // NOTE: This request does not support pagination.
    @GET("list")
    suspend fun fetchPhotos(): Response<List<Photo?>?>

    @GET("{width}/{height}")
    suspend fun fetchPhotoById(
        @Path("width") width: Int,
        @Path("height") height: Int,
        @Query("image") id: Int
    ): Response<ResponseBody>
}
