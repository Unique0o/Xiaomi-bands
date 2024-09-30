package com.example.logifitappp.ui.components.menu

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val icon: ImageVector,
    val title: String,
    val action: () -> Unit
)