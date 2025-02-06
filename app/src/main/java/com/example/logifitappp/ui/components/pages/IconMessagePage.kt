package com.example.logifitappp.ui.components.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.IconButton

@Composable
fun IconMessagePage(
    action: () -> Unit,
    buttonIcon: ImageVector,
    buttonLabel: String,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    message: String,
    pageIcon: ImageVector,
    topBar: @Composable () -> Unit = {}
) {
    SimplePage(topBar = topBar) {
        Column(
            Modifier.fillMaxSize().padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = pageIcon,
                contentDescription = "icon message page",
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(10.dp))

            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = message,
                textAlign = TextAlign.Center,
                typography = MaterialTheme.typography.labelMedium
            )

            Spacer(Modifier.height(10.dp))

            IconButton(
                icon = buttonIcon,
                onClick = action,
                text = buttonLabel,
            )
        }
    }
}