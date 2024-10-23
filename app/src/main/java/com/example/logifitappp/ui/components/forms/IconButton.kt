package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    verticalPadding: Dp = 16.dp,
    icon: ImageVector,
    onClick: () -> Unit,
    iconSize: Dp = 20.dp,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    cornerRadius: Dp = 24.dp,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(size = cornerRadius),
        containerColor = backgroundColor,
        elevation = elevation,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding, verticalPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                modifier = Modifier.size(iconSize),
                contentDescription = null,
                tint = textColor
            )

            Text(
                color = textColor,
                modifier = Modifier.padding(start = 5.dp),
                text = text,
                typography = MaterialTheme.typography.headlineMedium
            )
        }
    }
}