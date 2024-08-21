package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.theme.Stone470

@Composable
fun CardTimeLabels(
    hourStart: String,
    hourFinal: String,
    modifier: Modifier = Modifier,
    textColor: Color = Stone470,
    fontSize: TextUnit = 12.sp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = hourStart, fontSize = fontSize, color = textColor)
        Text(text = hourFinal, fontSize = fontSize, color = textColor)
    }
}