package com.example.logifitappp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconText(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconAction: (() -> Unit)? = null,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    iconSize: Dp = 24.dp,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    labelTypography: TextStyle = MaterialTheme.typography.bodyLarge,
    spaceBetween: Dp = 4.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (iconAction == null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )
        } else {
            IconButton(onClick = iconAction) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        Spacer(modifier = Modifier.width(spaceBetween))

        Text(
            color = labelColor,
            text = label,
            typography = labelTypography,
        )
    }
}