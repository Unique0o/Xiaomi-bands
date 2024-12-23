package com.example.logifitappp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    progressValues: List<Float>,
) {
    Box(modifier.clip(RoundedCornerShape(12.dp))) {
        Canvas(Modifier.fillMaxWidth().height(8.dp)) {
            val totalWidth = size.width
            var startX = 0f

            progressValues.zip(colors).forEach { (value, color) ->
                val segmentWidth = totalWidth * value

                drawRect(
                    color = color,
                    size = Size(width = segmentWidth, height = size.height),
                    topLeft = Offset(x = startX, y = 0f),
                )

                startX += segmentWidth
            }
        }
    }
}