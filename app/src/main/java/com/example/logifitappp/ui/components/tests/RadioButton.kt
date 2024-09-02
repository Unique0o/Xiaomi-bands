package com.example.logifitappp.ui.components.tests

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun RadioButton(
    selectedOption: Boolean,
    onOptionSelected: (Boolean) -> Unit,
    text: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selectedOption,
            onClick = {
                onOptionSelected(!selectedOption)
            }
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 2.dp),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }
}