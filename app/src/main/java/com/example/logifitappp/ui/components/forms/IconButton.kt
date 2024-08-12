package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    horizontalPadding: Dp = 24.dp,
    icon: ImageVector,
    onClick: () -> Unit,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(size = 24.dp),
        containerColor = backgroundColor,
        elevation = elevation,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor
            )

            Text(
                color = textColor,
                modifier = Modifier.padding(start = 8.dp),
                text = text,
                typography = MaterialTheme.typography.headlineMedium
            )
        }
    }
}