package com.example.logifitappp.ui.components.headers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.logifitappp.ui.components.forms.IconButton
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
            IconButton(
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                horizontalPadding = 0.dp,
                backgroundColor = Color.Transparent,
                icon = Icons.Filled.Person,
                onClick = { /* TODO */ },
                text = "",
                textColor = MaterialTheme.colorScheme.primary
            )
        }
    )
}