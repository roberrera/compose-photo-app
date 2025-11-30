package com.roberrera.resytakehome

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ResyTakeHomeApp: Application(), SingletonImageLoader.Factory {
    /**
     * Create an SingletonImageLoader instance for Coil to use so it can cache images.
     */
    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    // Set the max size to 25% of the app's available memory.
                    // This can be adjusted if performance needs tweaking.
                    .maxSizePercent(context,0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            // Set a crossfade transition when loading images.
            .crossfade(true)
            // Enable logging for debug builds.
            .apply { if (BuildConfig.DEBUG) logger(DebugLogger()) }
            .build()
    }

    private fun newDiskCache(): DiskCache {
        return DiskCache.Builder()
            .directory(applicationContext.cacheDir.resolve("image_cache"))
            .maxSizeBytes(512L * 1024 * 1024) // 512MB
            .build()
    }

}