package com.roberrera.resytakehome.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.roberrera.resytakehome.model.PhotosViewModel
import com.roberrera.resytakehome.network.Photo

@Composable
fun PhotosListScreen(viewModel: PhotosViewModel, onPhotoClick: (Photo) -> Unit) {

    val photos by viewModel.photos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchPhotos()
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = photos ?: emptyList()) { photo ->
                if (photo != null) {
                    Log.d("PhotosListScreen", "Photo: ${photo.fileName}")
                    PhotoRow(photo = photo, onPhotoClick = { onPhotoClick(photo) })
                }
            }
        }
    }
}

@Composable
fun PhotoRow(photo: Photo, onPhotoClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onPhotoClick),
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
