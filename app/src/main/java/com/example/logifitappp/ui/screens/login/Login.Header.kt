package com.example.logifitappp.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column {
            Image(
                painter = painterResource(
                    id = if (isSystemInDarkTheme()) R.drawable.ic_dark_logo else R.drawable.ic_light_logo
                ),
                contentDescription = null
            )

            Text(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = stringResource(id = R.string.subtitle_login),
                typography = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}