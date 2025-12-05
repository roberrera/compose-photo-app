package com.roberrera.composephotoapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun ComposephotoappNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = "photosList",
        modifier = modifier
    ) {
        composable(route = "photosList") {
            PhotosListScreen(
                onPhotoClick = { photo ->
                    navController.navigate(
                        "photoDetails/${photo.width}/${photo.height}/${photo.id}/${photo.author}"
                    )
                }
            )
        }
        composable(
            "photoDetails/{width}/{height}/{id}/{author}",
            arguments = listOf(
                navArgument("width") { type = NavType.IntType },
                navArgument("height") { type = NavType.IntType },
                navArgument("id") { type = NavType.IntType },
                navArgument("author") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getInt("id")
            val width = backStackEntry.arguments?.getInt("width") ?: 0
            val height = backStackEntry.arguments?.getInt("height") ?: 0
            val authorName = backStackEntry.arguments?.getString("author") ?: ""
            if (photoId != null) {
                PhotoDetailsScreen(
                    width,
                    height,
                    photoId,
                    authorName
                )
            }
        }
    }
}
