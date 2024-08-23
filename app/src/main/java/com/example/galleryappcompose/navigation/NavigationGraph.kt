package com.example.galleryappcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.galleryappcompose.ui.screens.Details
import com.example.galleryappcompose.ui.screens.Home
import com.example.galleryappcompose.ui.screens.VideoPlayer
import com.example.galleryappcompose.ui.viewmodels.MViewmodel
import com.example.galleryappcompose.ui.viewmodels.SharedViewModel

@Composable
fun NavGraph(viewModel: MViewmodel, sharedViewModel: SharedViewModel) {
    val navHostController: NavHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) {
            Home(viewModel, navHostController, sharedViewModel)
        }
        composable("${Screens.Details.route}/{photoUrl}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("photoUrl")
            Details(url, navHostController)
        }
        composable("${Screens.VideoPlayer.route}/{videoUrl}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("videoUrl")
            VideoPlayer(url, navHostController)
        }
    }
}