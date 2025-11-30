package com.roberrera.resytakehome.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.roberrera.resytakehome.model.PhotosViewModel
import com.roberrera.resytakehome.network.Photo

@Composable
fun PhotosListScreen(onPhotoClick: (Photo) -> Unit) {

    val photosViewModel: PhotosViewModel = hiltViewModel()
    val photos by photosViewModel.photos.collectAsState()
    val isLoading by photosViewModel.isLoading.collectAsState()
    val scrollState = rememberLazyListState()

    LaunchedEffect(true) {
        photosViewModel.fetchPhotos()
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            state = scrollState
        ) {
            items(items = photos ?: emptyList()) { photo ->
                photo?.let { PhotoRow(photo = it, onPhotoClick = { onPhotoClick(it) }) }
            }
        }
    }
}

@Composable
fun PhotoRow(photo: Photo, onPhotoClick: (() -> Unit)?) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = { onPhotoClick?.invoke() }),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = photo.fileName ?: ""
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoRowPreview() {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "filename.jpeg"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoRowListPreview() {
    val photoObject = Photo(
        id = 1,
        author = "",
        width = 0,
        height = 0,
        fileName = "",
        authorUrl = "",
        postUrl = ""
    )
    val photos = listOf(
        photoObject.copy(fileName = "0.jpeg"),
        photoObject.copy(fileName = "1.jpeg"),
        photoObject.copy(fileName = "2.jpeg"),
        photoObject.copy(fileName = "3.jpeg"),
        photoObject.copy(fileName = "4.jpeg")
    )

    LazyColumn {
        items(items = photos) {
            PhotoRow(photo = it, onPhotoClick = null)
        }
    }
}
