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
    // NOTE: This call seems to ignore the limit and return all results at once. We can leave this
    // implementation for now in case pagination gets added.
    @GET("list")
    suspend fun fetchPhotos(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30
    ): Response<List<Photo?>?>

    @GET("{width}/{height}")
    suspend fun fetchPhotoById(
        @Path("width") width: Int,
        @Path("height") height: Int,
        @Query("image") id: Int
    ): Response<ResponseBody>
}
