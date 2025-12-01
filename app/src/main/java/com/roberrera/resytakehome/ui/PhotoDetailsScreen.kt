package com.roberrera.resytakehome.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

private sealed class ImageLoadState {
    object Loading : ImageLoadState()
    data class Success(val bitmap: ImageBitmap) : ImageLoadState()
    object Error : ImageLoadState()
}

@Composable
fun PhotoDetailsScreen(width: Int, height: Int, id: Int, authorName: String) {

    val photosViewModel: PhotosViewModel = hiltViewModel()
    val imageLoaderViewModel: ImageLoaderInterfaceViewModel = hiltViewModel()
    val selectedPhotoUrl by photosViewModel.selectedPhotoUrl.collectAsState()
    val apiError by photosViewModel.error.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(id) {
        photosViewModel.clearSelectedPhotoUrl()
        photosViewModel.fetchPhotoById(width, height, id)
    }

    LaunchedEffect(apiError) {
        apiError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            photosViewModel.clearError()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = if (height < width) Alignment.Center else Alignment.TopCenter
    ) {
        if (selectedPhotoUrl != null) {
            AsyncImageWithCache(
                url = selectedPhotoUrl!!,
                authorName = authorName,
                contentDescription = "Image by $authorName",
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
fun AsyncImageWithCache(
    url: String,
    authorName: String,
    contentDescription: String?,
    imageLoaderInterface: ImageLoaderInterface
) {
    var imageState by remember(url) {
        mutableStateOf<ImageLoadState>(ImageLoadState.Loading)
    }

    LaunchedEffect(url) {
        if (url.isNotBlank()) {
            imageState = ImageLoadState.Loading
            val loadedBitmap = imageLoaderInterface.loadImage(url)
            imageState = if (loadedBitmap != null) {
                ImageLoadState.Success(loadedBitmap.asImageBitmap())
            } else {
                ImageLoadState.Error
            }
        } else {
            imageState = ImageLoadState.Error
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
                        Text(text = authorName)
                    }
                }
            }
            is ImageLoadState.Error -> {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error loading image",
                    modifier = Modifier.align(Alignment.Center)
                )
                Toast.makeText(
                    LocalContext.current,
                    "Error loading image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


@Preview(name = "Success State", showBackground = true)
@Composable
fun AsyncImageSuccessPreview() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxWidth()
            // In case the screen is too short
            .verticalScroll(rememberScrollState())
        ) {
            val drawable = ContextCompat.getDrawable(
                LocalContext.current,
                R.drawable.ic_launcher_background
            )
            val bitmap = drawable!!.toBitmap()
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "File name"
            )
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(text = "Author name")
            }
        }
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
fun AsyncImageLoadingPreview() {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(name = "Error State", showBackground = true)
@Composable
fun AsyncImageErrorPreview() {
    Box(Modifier.fillMaxSize()) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error loading image",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}