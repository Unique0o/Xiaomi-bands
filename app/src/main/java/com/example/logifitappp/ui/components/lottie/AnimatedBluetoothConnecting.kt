package com.example.logifitappp.ui.components.lottie

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.layouts.LottieLayout

@Composable
fun AnimatedBluetoothConnecting() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LottieLayout(
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.size(screenWidth * 0.8f),
        resource = R.raw.bluetooth_connecting
    )
}