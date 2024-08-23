package com.example.logifitappp.ui.components.home

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.forms.IconButton

@Composable
fun ShareButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    IconButton(
        icon = Icons.Default.Share,
        iconSize = 15.dp,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        text = title,
        onClick = onClick,
        verticalPadding = 0.dp,
        modifier = modifier.height(24.dp)

    )
}
