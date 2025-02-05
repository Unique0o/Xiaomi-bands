package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rectangle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun ChartLegendItem(
    modifier: Modifier = Modifier,
    color: Color,
    label: String
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            contentDescription = "summary dot",
            imageVector = Icons.Default.Rectangle,
            modifier = Modifier.size(12.dp),
            tint = color
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = label,
            typography = MaterialTheme.typography.labelMedium
        )
    }
}