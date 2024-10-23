package com.example.logifitappp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text as MaterialText

@Composable
fun Text(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    typography: TextStyle = MaterialTheme.typography.bodyLarge,
    text: String,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight = FontWeight.Normal
) {
    MaterialText(
        color = color,
        fontFamily = typography.fontFamily,
        fontSize = typography.fontSize,
        lineHeight = typography.lineHeight,
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        fontWeight = fontWeight
    )
}