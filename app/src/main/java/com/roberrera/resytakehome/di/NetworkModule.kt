package com.roberrera.resytakehome.di

import android.content.Context
import com.roberrera.resytakehome.network.ApiService
import com.roberrera.resytakehome.network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Hilt module for providing network dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://picsum.photos/"

    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context)
    : NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideJsonConverterFactory(): Converter.Factory {
        val json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
            isLenient = true
        }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
