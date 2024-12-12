package com.example.logifitappp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun Link(
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = text,
            typography = MaterialTheme.typography.bodySmall
        )
    }
}