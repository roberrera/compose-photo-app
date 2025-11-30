package com.roberrera.resytakehome.model

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ImageLoaderInterfaceViewModel @Inject constructor(
    application: Application
) : ViewModel(), ImageLoaderInterface {

    /**
     * The [memoryCache] is an in-memory cache that survives configuration changes.
     */
    private val memoryCache = object : LruCache<String, Bitmap>(50) {}

    /**
     * The [diskCacheDir] is a reference to the app's private cache directory.
     */
    private val diskCacheDir = File(
        application.cacheDir,
        "image_cache"
    ).also { it.mkdirs() }

    /**
     * Loads a bitmap from a URL, using memory and disk caches.
     */
    override suspend fun loadImage(url: String): Bitmap? {
        // Check the memory cache
        val memCachedBitmap = memoryCache[url]
        if (memCachedBitmap != null) {
            return memCachedBitmap
        }

        // If not in memory, check the disk and network on a background thread.
        return withContext(Dispatchers.IO) {
            try {
                val diskFile = File(diskCacheDir, url.hashCode().toString())

                // Check the disk cache
                if (diskFile.exists()) {
                    val diskBitmap = BitmapFactory.decodeFile(diskFile.absolutePath)
                    if (diskBitmap != null) {
                        memoryCache.put(url, diskBitmap) // Warm memory cache
                        return@withContext diskBitmap
                    }
                }

                // If the image is not on disk, download it from the network
                val connection = URL(url).openConnection() as java.net.HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                val bytes = inputStream.readBytes()
                diskFile.writeBytes(bytes) // Save it to disk

                // Decode, cache, and return
                val networkBitmap = BitmapFactory.decodeByteArray(
                    bytes,
                    0,
                    bytes.size
                )
                memoryCache.put(url, networkBitmap)
                networkBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
