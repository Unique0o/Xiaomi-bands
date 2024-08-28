package com.example.logifitappp.ui.components.statistics

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.KeyboardBackspace
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.theme.LogifitApppTheme


@Composable
fun RangeDateSelect(
    label: String,
    onLeftPress: () -> Unit,
    onRightPress: () -> Unit,
    labelStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Row(
        modifier = Modifier
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
            onClick = { /*TODO*/ },
            horizontalPadding=  0.dp,
            verticalPadding=  0.dp,
            text = "",
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(40.dp),
            cornerRadius = 10.dp
        )
        Text(
            text = label,
            style = labelStyle.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        IconButton(icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
            onClick = { /*TODO*/ },
            horizontalPadding=  0.dp,
            verticalPadding=  0.dp,
            text = "",
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(40.dp),
            cornerRadius = 10.dp


        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRangeDateSelect() {
    LogifitApppTheme {
        RangeDateSelect(
            label = "01/01/2024",
            onLeftPress = {},
            onRightPress = {}
        )
    }
}
