package com.example.logifitappp.ui.screens.unauthorized

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.pages.SimplePage

@Composable
fun UnauthorizedView() {
    SimplePage {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                contentDescription = null,
                imageVector = Icons.Default.NoAccounts,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.surfaceTint
            )

            Spacer(Modifier.height(10.dp))

            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(R.string.unauthorized_screen_title),
                textAlign = TextAlign.Center,
                typography = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(10.dp))

            Text(
                color = MaterialTheme.colorScheme.surfaceTint,
                text = stringResource(R.string.unauthorized_screen_message),
                textAlign = TextAlign.Center,
                typography = MaterialTheme.typography.labelMedium
            )
        }
    }
}