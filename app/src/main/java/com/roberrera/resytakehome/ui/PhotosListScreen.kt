package com.roberrera.resytakehome.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.roberrera.resytakehome.model.PhotosViewModel

@Composable
fun PhotosListScreen(viewModel: PhotosViewModel) {

    LaunchedEffect(Unit) {
        viewModel.fetchPhotos()
    }

    // TODO: Implement layout

}