package com.example.logifitappp.ui.components.Trainings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.components.graphics.bars.ProgressBar
import com.example.logifitappp.ui.theme.Stone470


@Composable
fun ProgressSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Progress 0.00%",
            fontSize = 14.sp,
            color = Color.Gray
        )
        ProgressBar(
            primaryProgressColor = Stone470,
            primaryProgressFraction = 1f,
            secondaryProgressFraction = 0f
        )
    }
}