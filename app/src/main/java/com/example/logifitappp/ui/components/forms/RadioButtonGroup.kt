package com.example.logifitappp.ui.components.forms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text

@Composable
fun <T : Any> RadioButtonGroup(
    modifier: Modifier = Modifier,
    onChangeValue: (value: T) -> Unit,
    options: List<T>,
    value: T?
) {
    Column(modifier) {
        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(20.dp)) {
                    RadioButton(
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.surfaceTint
                        ),
                        selected = option == value,
                        onClick = { onChangeValue(option) }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    color = MaterialTheme.colorScheme.surfaceTint,
                    text = option.toString(),
                    typography = MaterialTheme.typography.bodyMedium
                )
            }

            if (index + 1 < options.size) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}