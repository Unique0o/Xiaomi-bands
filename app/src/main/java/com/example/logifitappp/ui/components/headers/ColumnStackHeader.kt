package com.example.logifitappp.ui.components.headers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardBackspace
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.forms.IconButton

@Composable
fun ColumnStackHeader(
    navigation: NavHostController,
    title: String
) {
    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(
            backgroundColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            horizontalPadding = 0.dp,
            icon = Icons.AutoMirrored.Rounded.KeyboardBackspace,
            onClick = { navigation.popBackStack() },
            text = stringResource(id = R.string.go_back),
            textColor = MaterialTheme.colorScheme.primary
        )

        Text(
            text = title,
            typography = MaterialTheme.typography.displayLarge
        )
    }
}