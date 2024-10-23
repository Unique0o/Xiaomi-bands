package com.example.logifitappp.ui.components.layouts

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieLayout(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    iterations: Int = 1,
    @RawRes resource: Int
) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(resource)
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = iterations,
        isPlaying = isPlaying
    )

    LottieAnimation(
        composition = preloaderLottieComposition,
        modifier = modifier,
        progress = preloaderProgress
    )
}