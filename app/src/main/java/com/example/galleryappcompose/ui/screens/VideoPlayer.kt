package com.example.galleryappcompose.ui.screens

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.example.galleryappcompose.R
import com.example.galleryappcompose.ui.activity.MainActivity
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Preview
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrl: String? = "", navHostController: NavHostController? = null) {

    val context: Context = LocalContext.current
    val activity: MainActivity = context as MainActivity
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    var showController by remember { mutableStateOf(true) }
    var isVideoPlaying by remember { mutableStateOf(true) }

    val exoPlayer: ExoPlayer = remember { ExoPlayer.Builder(context).build() }
    val mediaSource: MediaItem = remember(videoUrl) { MediaItem.fromUri(videoUrl ?: "") }

    val window: Window = activity.window
    val windowInsetsController: WindowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.decorView)

    LaunchedEffect(showController) {
        if (showController) {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }
        delay(3000)
        showController = false
    }

    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.prepare()
        exoPlayer.play()
    }

    DisposableEffect(Unit) {
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (playerView) = createRefs()

        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .constrainAs(playerView) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        //Controller

        val modifier: Modifier =
            Modifier
                .fillMaxSize()
                //click without ripple effect
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        showController = !showController
                    })
                }
                .background(color = Color.Transparent)

        var sliderPosition by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                val watchedTimeInSec = exoPlayer.currentPosition.div(1000)
                sliderPosition = watchedTimeInSec.toFloat()
                delay(100)
            }
        }

        Controller(
            modifier = modifier,
            sliderPosition = sliderPosition,
            showController = showController,
            isVideoPlaying = isVideoPlaying,
            videoDuration = exoPlayer.duration,
            onValueChange = { value: Float ->
                sliderPosition = value

                exoPlayer.seekTo((value * 1000).toLong())
            },
            playPauseClick = {
                if (isVideoPlaying) exoPlayer.pause() else exoPlayer.play()
                isVideoPlaying = !isVideoPlaying
            }
        )
    }


}

@Preview
@Composable
private fun Controller(
    modifier: Modifier = Modifier.fillMaxSize(),
    sliderPosition: Float = 0.0f,
    showController: Boolean = true,
    isVideoPlaying: Boolean = false,
    videoDuration: Long = 0L,
    onValueChange: (Float) -> Unit = {},
    playPauseClick: () -> Unit = {}
) {
    ConstraintLayout(modifier = modifier) {

        val (image,slider, playPause, progress, duration) = createRefs()

        if (showController) {

            Image(
                painter = painterResource(
                    id = R.drawable.baseline_arrow_back_24
                ),
                contentDescription = null,
                modifier = Modifier.constrainAs(image) {
                    top.linkTo(parent.top, 15.dp)
                    start.linkTo(parent.start, 15.dp)
                }
            )

            Text(text = "00", color = Color.White, modifier = Modifier.constrainAs(progress) {
                end.linkTo(slider.start, 5.dp)
                top.linkTo(slider.top)
                bottom.linkTo(slider.bottom)
            })

            val maxValue = if(videoDuration >= 0) (videoDuration / 1000).toFloat() else 10f

            Slider(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .constrainAs(slider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, 15.dp)
                    },
                value = sliderPosition, onValueChange = { onValueChange.invoke(it) },
                valueRange = 0f..maxValue,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Black,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Black
                )
            )

            Text(text = videoDuration.toString(), color = Color.White, modifier = Modifier.constrainAs(duration) {
                start.linkTo(slider.end, 5.dp)
                top.linkTo(slider.top)
                bottom.linkTo(slider.bottom)
            })

            Image(
                painter = painterResource(id = if (isVideoPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clickable { playPauseClick.invoke() }
                    .constrainAs(playPause) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

        }
    }
}