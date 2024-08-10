package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    MaterialButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier,
        shape = RoundedCornerShape(size = 24.dp)
    ) {
        Text(
            color = MaterialTheme.colorScheme.onPrimary,
            text = text,
            typography = MaterialTheme.typography.headlineMedium
        )
    }
}