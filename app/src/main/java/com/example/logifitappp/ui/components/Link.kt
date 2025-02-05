package com.example.logifitappp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Link(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            color = textColor,
            text = text,
            typography = MaterialTheme.typography.bodySmall
        )
    }
}