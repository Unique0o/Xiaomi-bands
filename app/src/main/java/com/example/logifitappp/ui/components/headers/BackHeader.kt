package com.example.logifitappp.ui.components.headers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardBackspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun BackHeader(
    navigation: NavHostController,
    title: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                horizontalPadding = 20.dp,
                backgroundColor = Color.Transparent,
                icon = Icons.AutoMirrored.Rounded.KeyboardBackspace,
                onClick = { navigation.popBackStack() },
                text = "",
                textColor = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = Blue690,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 34.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BackHeaderPreview() {
    LogifitApppTheme {
        BackHeader(
            navigation = rememberNavController(),
            title = "Sample Title"
        )
    }
}

