package com.example.logifitappp.ui.components

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.logifitappp.core.utils.DurationUtils

@Composable
fun Video(
    onEnd: () -> Unit = {},
    uri: String
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
        }
    }

    var showControls by remember { mutableStateOf(true) }
    var isFullScreen by remember { mutableStateOf(false) }

    var currentPosition by remember { mutableLongStateOf(0L) }
    var totalDuration by remember { mutableLongStateOf(0L) }

    val mHandler = remember { Handler(Looper.getMainLooper()) }
    val hideControls = remember {
        Runnable {
            showControls = false
        }
    }

    fun resetAutoHideTimer() {
        mHandler.removeCallbacks(hideControls)
        mHandler.postDelayed(hideControls, 3000L)
    }

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        val updateProgress = object: Runnable {
            override fun run() {
                currentPosition = exoPlayer.currentPosition
                handler.postDelayed(this, 500L)
            }
        }

        handler.post(updateProgress)
        resetAutoHideTimer()

        val listener = object: Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) totalDuration = exoPlayer.duration

                if (playbackState == Player.STATE_ENDED) onEnd()
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            handler.removeCallbacks(updateProgress)
            mHandler.removeCallbacks(hideControls)
            exoPlayer.release()
        }
    }

    Box(
        modifier = (if (isFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth().height(250.dp))
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    showControls = true
                    resetAutoHideTimer()
                })
            }
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (showControls) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VolumeControl(exoPlayer)

                    /*IconButton(
                        onClick = {
                            isFullScreen = !isFullScreen

                            if (isFullScreen) (context as Activity).enterFullscreenMode()
                            else (context as Activity).exitFullscreenMode()
                        }
                    ) {
                        Icon(
                            contentDescription = "toggle fullscreen",
                            imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            tint = Color.White
                        )
                    }*/
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                    }) {
                        Icon(
                            contentDescription = "play/pause",
                            imageVector = if (exoPlayer.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            tint = Color.White
                        )
                    }

                    Slider(
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color.White,
                            thumbColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onValueChange = { value ->
                            val seekPosition = (value * totalDuration).toLong()
                            exoPlayer.seekTo(seekPosition)
                            currentPosition = seekPosition
                        },
                        value = when {
                            totalDuration > 0 -> currentPosition.toFloat() / totalDuration.toFloat()
                            else -> 0f
                        }
                    )

                    Spacer(Modifier.width(2.dp))

                    Text(
                        color = Color.White,
                        text = DurationUtils.formatTime((totalDuration - currentPosition) / 1000),
                        typography = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun VolumeControl(exoPlayer: ExoPlayer) {
    var currentVolume by remember { mutableFloatStateOf(exoPlayer.volume) }

    val icon = when {
        currentVolume == 0f -> Icons.AutoMirrored.Filled.VolumeOff
        currentVolume < 50f -> Icons.AutoMirrored.Filled.VolumeDown
        else -> Icons.AutoMirrored.Filled.VolumeUp
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            contentDescription = "volume control",
            imageVector = icon,
            tint = Color.White
        )

        Spacer(Modifier.width(2.dp))

        Slider(
            colors = SliderDefaults.colors(
                activeTrackColor = Color.White,
                thumbColor = Color.White
            ),
            modifier = Modifier.width(90.dp),
            onValueChange = {
                currentVolume = it
                exoPlayer.volume = it
            },
            value = currentVolume,
            valueRange = 0f..1f
        )
    }
}