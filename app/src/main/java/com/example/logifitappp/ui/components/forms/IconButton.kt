package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    icon: ImageVector,
    onClick: () -> Unit,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    ExtendedFloatingActionButton(
        containerColor = backgroundColor,
        elevation = elevation,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor
            )
        },
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(size = 24.dp),
        text = {
            Text(
                color = textColor,
                text = text,
                typography = MaterialTheme.typography.headlineMedium
            )
        }
    )
}