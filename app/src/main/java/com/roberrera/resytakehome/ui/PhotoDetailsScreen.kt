package com.roberrera.resytakehome.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.roberrera.resytakehome.R
import com.roberrera.resytakehome.model.ImageLoaderInterface
import com.roberrera.resytakehome.model.ImageLoaderInterfaceViewModel
import com.roberrera.resytakehome.model.PhotosViewModel
import kotlinx.coroutines.delay

private sealed class ImageLoadState {
    object Loading : ImageLoadState()
    data class Success(val bitmap: ImageBitmap) : ImageLoadState()
    object Error : ImageLoadState()
}

@Composable
fun PhotoDetailsScreen(width: Int, height: Int, id: Int, authorName: String) {

    val photosViewModel: PhotosViewModel = hiltViewModel()
    // Get the shared ViewModel that holds the image caching logic.
    val imageLoaderViewModel: ImageLoaderInterfaceViewModel = hiltViewModel()
    // Get the URL returned from the API request and observe if that URL changes.
    val selectedPhotoUrl by photosViewModel.selectedPhotoUrl.collectAsState()

    /**
     * Clear the previously loaded photo URL and request the new image. This ensures we don't see
     * the previous image during the loading state.
     */
    LaunchedEffect(id) {
        photosViewModel.clearSelectedPhotoUrl()
        photosViewModel.fetchPhotoById(width, height, id)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        // Implement image alignment logic
        contentAlignment = if (height < width) Alignment.Center else Alignment.TopCenter
    ) {
        if (selectedPhotoUrl != null) {
            // Display the image from the returned image file URL
            ManualAsyncImageWithCache(
                url = selectedPhotoUrl!!,
                authorName = authorName,
                contentDescription = "Image by $authorName",
                // Pass the view model, which implements the ImageLoaderInterface.
                imageLoaderInterface = imageLoaderViewModel
            )
        } else {
            CircularProgressIndicator()
        }
    }
}

/**
 * A composable that delegates image loading and caching to a shared [ImageLoaderInterface]
 * implementation.
 */
@Composable
fun ManualAsyncImageWithCache(
    url: String,
    authorName: String,
    contentDescription: String?,
    imageLoaderInterface: ImageLoaderInterface
) {
    var imageState by remember(url) { mutableStateOf<ImageLoadState>(ImageLoadState.Loading) }

    LaunchedEffect(url) {
        if (url.isNotBlank()) {
            imageState = ImageLoadState.Loading
            val loadedBitmap = imageLoaderInterface.loadImage(url)
            imageState = if (loadedBitmap != null) {
                ImageLoadState.Success(loadedBitmap.asImageBitmap())
            } else {
                ImageLoadState.Error
            }
        }
    }

    // Display the image if it's ready, or else a loading indicator.
    Box {
        when (val state = imageState) {
            is ImageLoadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ImageLoadState.Success -> {
                Column(
                    // In case the screen is too short
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Image(bitmap = state.bitmap, contentDescription = contentDescription)
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = authorName
                        )
                    }
                }
            }
            is ImageLoadState.Error -> {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error loading image",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Preview(name = "Success State", showBackground = true)
@Composable
fun PhotoDetailPreview_Success() {
    val context = LocalContext.current
    // Create a fake ImageLoader that returns a placeholder bitmap from drawable resources.
    val fakeImageLoaderInterface = object : ImageLoaderInterface {
        override suspend fun loadImage(url: String): Bitmap {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
            return drawable!!.toBitmap()
        }
    }
    Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ManualAsyncImageWithCache(
            url = "fake_url",
            authorName = "Author name",
            contentDescription = "Preview Image",
            imageLoaderInterface = fakeImageLoaderInterface
        )
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "Author name"
            )
        }
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
fun PhotoDetailPreview_Loading() {
    val fakeImageLoaderInterface = object : ImageLoaderInterface {
        override suspend fun loadImage(url: String): Bitmap? {
            delay(Long.MAX_VALUE) // Simulates indefinite loading
            return null
        }
    }
    ManualAsyncImageWithCache(
        url = "fake_url",
        authorName = "Author name",
        contentDescription = "Preview Image",
        imageLoaderInterface = fakeImageLoaderInterface
    )
}

@Preview(name = "Error State", showBackground = true)
@Composable
fun PhotoDetailPreview_Error() {
    val fakeImageLoaderInterface = object : ImageLoaderInterface {
        override suspend fun loadImage(url: String): Bitmap? {
            return null // Simulates a network error
        }
    }
    ManualAsyncImageWithCache(
        url = "fake_url",
        authorName = "Author name",
        contentDescription = "Preview Image",
        imageLoaderInterface = fakeImageLoaderInterface
    )
}
