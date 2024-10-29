package com.example.logifitappp.ui.components.layouts

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CardLayout(
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.outline,
    content: @Composable () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = background),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        content()
    }
}