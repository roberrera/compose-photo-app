package com.roberrera.composephotoapp.testutils

import com.roberrera.composephotoapp.network.ApiService
import com.roberrera.composephotoapp.network.NoConnectivityException
import com.roberrera.composephotoapp.network.Photo
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

/**
 * Test helpers for fake ApiService implementations for unit tests.
 */
fun successApiService(body: List<Photo>): ApiService = object : ApiService {
    override suspend fun fetchPhotos(): Response<List<Photo?>?> {
        return Response.success(body)
    }

    override suspend fun fetchPhotoById(width: Int, height: Int, id: Int): Response<ResponseBody> {
        return Response.success("".toResponseBody(null))
    }
}

fun failingApiService(): ApiService = object : ApiService {
    override suspend fun fetchPhotos(): Response<List<Photo?>?> {
        throw NoConnectivityException()
    }

    override suspend fun fetchPhotoById(width: Int, height: Int, id: Int): Response<ResponseBody> {
        throw NoConnectivityException()
    }
}

