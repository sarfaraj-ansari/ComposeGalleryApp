package com.example.galleryappcompose.ui.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.galleryappcompose.ui.screens.App
import com.example.galleryappcompose.ui.viewmodels.MViewmodel
import com.example.galleryappcompose.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MViewmodel>()
    private val sharedViewModel by viewModels<SharedViewModel>()
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        println("oncreated-------")

        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT
        ))

        setContent {

            Scaffold(
                Modifier.statusBarsPadding().navigationBarsPadding()
            ) {

                App(viewModel = viewModel, sharedViewModel = sharedViewModel)

            }

        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        println("configuration changed---")
    }

    override fun onUserLeaveHint() {
        this.enterPictureInPictureMode()
    }
}
