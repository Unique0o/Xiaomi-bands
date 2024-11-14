package com.example.logifitappp.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.layouts.CardLayout

@Composable
fun SleepParameterCard(
    @DrawableRes image: Int,
    label: String,
    suffix: @Composable () -> Unit
) {
    CardLayout(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(contentDescription = null, painter = painterResource(image), modifier = Modifier.size(30.dp))
            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = label,
                    typography = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(8.dp))

                suffix()
            }
        }
    }
}