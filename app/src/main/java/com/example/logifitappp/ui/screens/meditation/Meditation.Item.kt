package com.example.logifitappp.ui.screens.meditation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.enums.AudioFileEnum
import com.example.logifitappp.ui.components.ProgressBar
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.lottie.AnimatedAudioWave
import com.example.logifitappp.ui.theme.Orange390

@Composable
fun MeditationItem(
    isPlaying: Boolean,
    isProgressVisible: Boolean,
    item: AudioFileEnum,
    progress: Float
) {
    val progressAlpha by animateFloatAsState(
        animationSpec = tween(durationMillis = 300),
        targetValue = if (isProgressVisible) 1f else 0f
    )

    Box(Modifier.fillMaxSize().background(Brush.linearGradient(item.gradientList))) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp),
                text = item.label,
                textAlign = TextAlign.Center,
                typography = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.height(8.dp))

            Icon(
                contentDescription = null,
                imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                tint = Color.White
            )
        }

        Box(Modifier.fillMaxSize().alpha(progressAlpha)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(listOf(Color(0xffd9ed92), Color(0xff99d98c), Color(0xff168aad))))
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedAudioWave(isPlaying)

                ProgressBar(
                    backgroundColor = Color.White.copy(alpha = .65f),
                    colors = listOf(Orange390),
                    height = 6.5.dp,
                    progressValues = listOf(progress)
                )
            }
        }
    }
}