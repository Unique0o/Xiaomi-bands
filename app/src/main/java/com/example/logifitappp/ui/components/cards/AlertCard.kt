package com.example.logifitappp.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun AlertCard(
    modifier: Modifier = Modifier,
    message: String
) {
    Box(
        Modifier.fillMaxWidth().then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.error, RoundedCornerShape(12.dp))
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = message,
                typography = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}