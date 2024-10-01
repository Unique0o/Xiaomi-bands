package com.example.logifitappp.ui.components.graphics.bars
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Sky320
import com.example.logifitappp.ui.theme.Violet500

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Sky320,
    primaryProgressColor: Color = Blue690,
    secondaryProgressColor: Color = Violet500,
    tertiaryProgressColor: Color? = null,
    primaryProgressFraction: Float = 0.76f,
    secondaryProgressFraction: Float = 0.11f,
    tertiaryProgressFraction: Float = 0f
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(backgroundColor, RoundedCornerShape(4.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(primaryProgressFraction)
                .height(8.dp)
                .background(primaryProgressColor, RoundedCornerShape(4.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(secondaryProgressFraction)
                .height(8.dp)
                .background(secondaryProgressColor, RoundedCornerShape(4.dp))
        )

        if (tertiaryProgressColor != null && tertiaryProgressFraction > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(tertiaryProgressFraction)
                    .height(8.dp)
                    .background(tertiaryProgressColor, RoundedCornerShape(4.dp))
            )
        }
    }
}