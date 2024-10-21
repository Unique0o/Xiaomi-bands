package com.example.logifitappp.ui.components.lottie

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.layouts.LottieLayout

@Composable
fun AnimatedWarningSignal() {
    LottieLayout(
        modifier = Modifier.width(96.dp).height(96.dp),
        resource = R.raw.warning
    )
}