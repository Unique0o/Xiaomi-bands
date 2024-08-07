package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun IconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    ExtendedFloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(size = 24.dp),
        text = {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = text,
                typography = MaterialTheme.typography.headlineMedium
            )
        }
    )
}