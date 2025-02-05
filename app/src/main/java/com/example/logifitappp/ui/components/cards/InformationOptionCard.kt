package com.example.logifitappp.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.IconText
import com.example.logifitappp.ui.components.Text

@Composable
fun InformationOptionCard(
    modifier: Modifier = Modifier,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    buttonIcon: ImageVector,
    icon: ImageVector,
    onClick: () -> Unit,
    onTitleClick: (() -> Unit)? = null,
    paragraph: String,
    title: String,
    bodyComponent: @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        IconText(
            icon = icon,
            iconColor = MaterialTheme.colorScheme.onSurface,
            iconSize = 30.dp,
            label = title,
            labelTypography = MaterialTheme.typography.displayMedium,
            modifier = if (onTitleClick == null) Modifier else Modifier.clickable { onTitleClick() }
        )

        Spacer(modifier = Modifier.height(6.dp))

        InformationOptionCardContent(
            buttonColor,
            buttonIcon,
            onClick,
            paragraph
        )

        bodyComponent()
    }
}

@Composable
fun InformationOptionCardContent(
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    buttonIcon: ImageVector,
    onClick: () -> Unit,
    paragraph: String,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            text = paragraph,
            typography = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onClick,
            modifier = Modifier
                .background(buttonColor, CircleShape)
                .size(40.dp)
        ) {
            Icon(
                contentDescription = null,
                imageVector = buttonIcon,
                tint = Color.White
            )
        }
    }
}