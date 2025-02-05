package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.lang.Error

@Composable
fun ClickableOutlinedTextWithCustomPlaceholder(
    value: String,
    error: String? = null,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline) // Outline of the Text
            .padding(1.dp)
            .clickable(onClick = {
                onClick()
            })
    ) {
        Text(
            text = if (error.isNullOrEmpty()) value else error,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray), // Placeholder style
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp) // Adjust padding for placeholder
        )

    }
}
