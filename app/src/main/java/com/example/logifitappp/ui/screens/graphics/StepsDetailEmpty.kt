package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.logifitappp.ui.components.graphics.EmptyCardHour
import com.example.logifitappp.ui.components.headers.BackHeader
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70

@Composable
fun StepsDetailEmpty() {
    val stepData = List(24) { (1..50).random() }

    Scaffold(
        topBar = {
            BackHeader(
                onBackClick = {  },
                subtitle = "Mis pasos",
                modifier = Modifier.background(Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyCardHour(
                    steps = stepData,
                    maxValue = 50,
                    barColor = Lime70,
                    accentColor = Green298,
                    title = "Pasos realizados",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepsDetailEmptyPreview() {
    StepsDetailEmpty()
}
