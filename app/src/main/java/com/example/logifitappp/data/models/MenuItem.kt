package com.example.logifitappp.data.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val icon: ImageVector,
    @StringRes val title: Int,
    val action: () -> Unit
)