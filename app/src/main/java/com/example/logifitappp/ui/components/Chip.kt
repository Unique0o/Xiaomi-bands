package com.example.logifitappp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.enums.ChipStatusEnum

@Composable
fun Chip(
    label: String,
    status: ChipStatusEnum
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .background(status.backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(status.color, CircleShape)
        )

        Text(
            color = status.color,
            text = label,
            typography = MaterialTheme.typography.titleMedium
        )
    }
}