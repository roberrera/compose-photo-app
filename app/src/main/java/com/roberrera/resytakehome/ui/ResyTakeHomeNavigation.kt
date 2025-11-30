package com.roberrera.resytakehome.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.roberrera.resytakehome.model.PhotosViewModel

@Composable
fun ResyTakeHomeNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val photosViewModel: PhotosViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "photosList",
        modifier = modifier
        ) {
        composable(route = "photosList") {
            PhotosListScreen(
                viewModel = photosViewModel,
                onPhotoClick = { photo ->
                    navController.navigate(
                        "photoDetails/${photo.width}/${photo.height}/${photo.id}/${photo
                            .fileName}"
                    )
                }
            )
        }
        composable(
            "photoDetails/{width}/{height}/{id}/{filename}",
            arguments = listOf(
                navArgument("width") { type = NavType.IntType },
                navArgument("height") { type = NavType.IntType },
                navArgument("id") { type = NavType.IntType },
                navArgument("filename") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getInt("id")
            val width = backStackEntry.arguments?.getInt("width") ?: 0
            val height = backStackEntry.arguments?.getInt("height") ?: 0
            val filename = backStackEntry.arguments?.getString("filename") ?: ""
            if (photoId != null) {
                PhotoDetailsScreen(
                    width,
                    height,
                    photoId,
                    filename,
                    photosViewModel
                )
            }
        }
    }
}
