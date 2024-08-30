package com.example.logifitappp.ui.components.statistics

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun RangeDateSelect(
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outline, RectangleShape)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
            iconSize = 10.dp,
            onClick = { /* TODO */ },
            horizontalPadding = 10.dp,
            verticalPadding = 0.dp,
            text = "",
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(30.dp),
            cornerRadius = 5.dp
        )
        Text(
            text = label,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 15.sp)
        )
        IconButton(
            icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
            iconSize = 10.dp,
            onClick = { /* TODO */ },
            horizontalPadding = 10.dp,
            verticalPadding = 0.dp,
            text = "",
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(30.dp),
            cornerRadius = 5.dp
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewRangeDateSelect() {
    LogifitApppTheme {
        RangeDateSelect(
            label = "01/01/2024"
        )
    }
}
