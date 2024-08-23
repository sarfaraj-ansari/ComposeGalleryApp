package com.example.galleryappcompose.navigation

sealed class Screens(val route: String) {
    data object Home: Screens("Home")
    data object Details: Screens("Details")
    data object VideoPlayer: Screens("VideoPlayer")
}