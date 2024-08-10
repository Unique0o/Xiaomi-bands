package com.example.logifitappp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R

@Composable
fun PasswordVisibilityToggleText(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    Text(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clickable {
            onTogglePasswordVisibility()
        },
        typography = MaterialTheme.typography.bodySmall,
        text = stringResource(id = if (showPassword) R.string.link_hide else R.string.link_show)
    )
}