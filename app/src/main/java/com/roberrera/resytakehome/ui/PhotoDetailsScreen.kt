package com.roberrera.resytakehome.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import coil3.compose.AsyncImage
import com.roberrera.resytakehome.model.PhotosViewModel

@Composable
fun PhotoDetailsScreen(width: Int, height: Int, id: Int, filename: String, viewModel: PhotosViewModel) {
    val selectedPhotoUrl by viewModel.selectedPhotoUrl.collectAsState()

    /**
     * Clear the previously loaded photo URL and request the new image. This ensures we don't see
     * the previous image during the loading state.
     */
    LaunchedEffect(id) {
        viewModel.clearSelectedPhotoUrl()
        viewModel.fetchPhotoById(width,height,id)
    }

    /**
     * If the image is landscape orientation, display it centered. If it's portroit orientation,
     * display it at the top.
     */
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment =
            if(height < width) {
                Alignment.Center
            } else {
                Alignment.TopCenter
            }
    ) {
        if (selectedPhotoUrl != null) {
            Column {
                // Display the image from the returned image file URL
                AsyncImage(
                    modifier = Modifier
                        .width(width.dp),
                    model = selectedPhotoUrl,
                    contentDescription = filename
                )
                Box(modifier = Modifier.padding(8.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = filename
                    )
                }
            }
        } else {
            // Display the loading state
            CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoDetailPreview() {
    AsyncImage(
        modifier = Modifier.fillMaxWidth()
            .height(250.dp),
        model = "https://fastly.picsum.photos/id/3/5000/3333.jpg",
        contentDescription = "",
        alignment = Alignment.Center
    )
    Box(
        modifier = Modifier.padding(8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = "filename.jpeg"
        )
    }
}
