package com.example.logifitappp.ui.components.lottie

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.layouts.LottieLayout

@Composable
fun AnimatedBluetoothConnection() {
    LottieLayout(
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.size(136.dp),
        resource = R.raw.bluetooth_connection
    )
}