package com.example.logifitappp.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.layouts.CardLayout

@Composable
fun InformationCard(
    icon: ImageVector,
    iconSize: Dp = 24.dp,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.primary,
    labelTypography: TextStyle = MaterialTheme.typography.bodyMedium,
    suffixComponent: (@Composable () -> Unit)? = null,
    bodyComponent: (@Composable () -> Unit)? = null
) {
    CardLayout {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconText(
                    icon = icon,
                    iconColor = MaterialTheme.colorScheme.primary,
                    iconSize = iconSize,
                    label = label,
                    labelColor = labelColor,
                    labelTypography = labelTypography,
                )

                Spacer(modifier = Modifier.weight(1f))

                suffixComponent?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    it()
                }
            }

            bodyComponent?.let {
                Spacer(Modifier.height(12.dp))
                it()
            }
        }
    }
}