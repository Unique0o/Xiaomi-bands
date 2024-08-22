package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun BackgroundCurve(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cornerRadius = 40f
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.3f - cornerRadius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    size.width - cornerRadius * 2,
                    size.height * 0.3f - cornerRadius * 2,
                    size.width,
                    size.height * 0.3f
                ), startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            quadraticTo(
                size.width / 2f, size.height * 0.3f + 40f, cornerRadius, size.height * 0.3f
            )
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    0f, size.height * 0.3f - cornerRadius * 2, cornerRadius * 2, size.height * 0.3f
                ), startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
            )
            close()
        }
        drawPath(path, Color.White)
    }
}