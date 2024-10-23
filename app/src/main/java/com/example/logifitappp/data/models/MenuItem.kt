package com.example.logifitappp.data.models

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val icon: ImageVector,
    val title: Int,
    val action: () -> Unit
)