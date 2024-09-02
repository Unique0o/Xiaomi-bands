package com.example.logifitappp.ui.components.graphics

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.theme.White

@Composable
fun HeaderRow(date: String, title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                onClick = { /*TODO*/ },
                horizontalPadding=  0.dp,
                verticalPadding=  0.dp,
                text = "",
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = Color.Gray,
                modifier = Modifier.size(40.dp),
                cornerRadius = 10.dp,
                elevation = FloatingActionButtonDefaults.elevation( 0.dp )
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.inverseSurface
            )
            IconButton(
                icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                onClick = { /*TODO*/ },
                horizontalPadding=  0.dp,
                verticalPadding=  0.dp,
                text = "",
                backgroundColor = MaterialTheme.colorScheme.surface,
                textColor = Color.Gray,
                modifier = Modifier.size(40.dp),
                cornerRadius = 10.dp,
                elevation = FloatingActionButtonDefaults.elevation( 0.dp )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.inverseSurface
        )
    }
}

@Preview
@Composable
fun HeaderRowPreview() {
    HeaderRow(date = "Noviembre 20, 2023", title = "My Heart Rate")
}