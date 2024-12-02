package com.example.logifitappp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.Text as MaterialText

@Composable
fun Text(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    overflow: TextOverflow = TextOverflow.Clip,
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
        maxLines = maxLines,
        modifier = modifier,
        onTextLayout = onTextLayout,
        overflow = overflow,
        text = text,
        textAlign = textAlign,
        fontWeight = fontWeight
    )
}