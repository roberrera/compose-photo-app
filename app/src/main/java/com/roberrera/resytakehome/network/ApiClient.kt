package com.roberrera.resytakehome.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object ApiClient {

    private const val BASE_URL = "https://picsum.photos/"
    private var retrofit: Retrofit? = null
    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient : OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
    }.build()

    fun getInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
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
    // NOTE: This call seems to ignore the limit and return all results at once.
    // There is a v2 request, but the Photo objects returned don't include a filename, which
    // we want to display.
    @GET("list")
    suspend fun fetchPhotos(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30
    ) : Response<List<Photo?>?>

    @GET("{width}/{height}")
    suspend fun fetchPhotoById(
        @Path("width") width: Int,
        @Path("height") height: Int,
        @Query("image") id: Int
    ) : Response<ResponseBody>

}
