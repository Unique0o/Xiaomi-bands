package com.example.logifitappp.ui.components.headers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.logifitappp.ui.theme.Blue690


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    modifier = modifier.align(Alignment.Center),
                    color = Blue690,
                    style = MaterialTheme.typography.titleLarge
                )
            }

        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        actions = {
            IconButton(onClick = { /* Acción del perfil */ }) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Blue690
                )
            }
        }
    )
}