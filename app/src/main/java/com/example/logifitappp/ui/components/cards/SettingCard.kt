package com.example.logifitappp.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun SettingCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    summary: String,
    title: String,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(Modifier.weight(1f)) {
            Text(
                color = MaterialTheme.colorScheme.surfaceTint,
                text = title,
                typography = MaterialTheme.typography.headlineLarge
            )

            Text(
                color = MaterialTheme.colorScheme.surfaceTint,
                text = summary,
                typography = MaterialTheme.typography.labelMedium
            )
        }
    }
}