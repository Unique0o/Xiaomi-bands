package com.example.logifitappp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R

@Composable
fun PasswordVisibilityToggleText(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    Link(
        text = stringResource(id = if (showPassword) R.string.link_hide else R.string.link_show)
    ) { onTogglePasswordVisibility() }
}