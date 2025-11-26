package com.roberrera.resytakehome.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiClient {

    private const val BASE_URL = "https://picsum.photos/"
    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(generateConverterFactory())
                .build()
        }
        return retrofit!!
    }

    private fun generateConverterFactory() : Converter.Factory {
        val json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
            isLenient = true
        }
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }

}

interface ApiService {
    @GET("photos")
    suspend fun fetchPhotos(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ) : Response<ApiResponse>

}