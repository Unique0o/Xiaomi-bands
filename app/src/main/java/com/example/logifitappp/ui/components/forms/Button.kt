package com.example.logifitappp.ui.components.forms


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button as MaterialButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    containerColors: Color = MaterialTheme.colorScheme.primary,
    colorText: Color = MaterialTheme.colorScheme.onPrimary,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp)
) {
    MaterialButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = containerColors),
        modifier = modifier,
        shape = shape,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Text(
            color = colorText,
            text = text,
            typography = MaterialTheme.typography.headlineMedium
        )
    }
}