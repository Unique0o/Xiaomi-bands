package com.example.logifitappp.ui.components.lottie

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.layouts.LottieLayout

@Composable
fun AnimatedAudioWave(
    isPlaying: Boolean
) {
    LottieLayout(
        isPlaying = isPlaying,
        iterations = Int.MAX_VALUE,
        modifier = Modifier.size(120.dp),
        resource = R.raw.audio_wave
    )
}