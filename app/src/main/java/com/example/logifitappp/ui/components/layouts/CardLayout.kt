package com.example.logifitappp.ui.components.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

@Composable
fun CardLayout(
    modifier: Modifier = Modifier,
    bodyComponent: @Composable () -> Unit = {},
    icon: ImageVector,
    iconSize: Dp = 24.dp,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    labelTypography: TextStyle = MaterialTheme.typography.bodyMedium,
    suffixComponent: (@Composable () -> Unit)? = null,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.outline
        ),
        modifier = modifier
    ) {
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

            bodyComponent()
        }
    }
}