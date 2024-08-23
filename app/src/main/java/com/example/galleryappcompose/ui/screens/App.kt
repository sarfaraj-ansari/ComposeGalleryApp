package com.example.galleryappcompose.ui.screens

import androidx.compose.runtime.Composable
import com.example.galleryappcompose.navigation.NavGraph
import com.example.galleryappcompose.ui.viewmodels.MViewmodel
import com.example.galleryappcompose.ui.viewmodels.SharedViewModel

@Composable
fun App(viewModel: MViewmodel, sharedViewModel: SharedViewModel) {
    NavGraph(viewModel = viewModel, sharedViewModel = sharedViewModel)
}