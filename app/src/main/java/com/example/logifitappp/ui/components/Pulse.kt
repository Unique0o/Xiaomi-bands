package com.example.logifitappp.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.theme.Rose120

@Composable
fun Pulse(
    color: Color = Rose120,
    intervalTime: Int = 500,
    size: Dp = 7.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse transition")

    val scale by infiniteTransition.animateFloat(
        animationSpec = infiniteRepeatable(
            animation = tween(intervalTime, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        initialValue = 1f,
        label = "pulse scale",
        targetValue = 1.5f
    )

    val alpha by infiniteTransition.animateFloat(
        animationSpec = infiniteRepeatable(
            animation = tween(intervalTime, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        initialValue = 1f,
        label = "pulse alpha",
        targetValue = 1.5f
    )

    Box(
        Modifier
            .size(size)
            .scale(scale)
            .background(color.copy(alpha = alpha), shape = CircleShape)
    )
}