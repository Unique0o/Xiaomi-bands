package com.example.logifitappp.ui.components.lottie

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.layouts.LottieLayout

@Composable
fun AnimatedWarningSignal() {
    LottieLayout(
        modifier = Modifier.size(96.dp),
        resource = R.raw.warning
    )
}