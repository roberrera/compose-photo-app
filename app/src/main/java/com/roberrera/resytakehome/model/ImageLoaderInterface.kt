package com.roberrera.resytakehome.model

import android.graphics.Bitmap

/**
 * An interface for loading an image from a URL without a third-party library.
 * This is needed for creating fake implementations for previews and tests.
 */
interface ImageLoaderInterface {
    suspend fun loadImage(url: String): Bitmap?
}
