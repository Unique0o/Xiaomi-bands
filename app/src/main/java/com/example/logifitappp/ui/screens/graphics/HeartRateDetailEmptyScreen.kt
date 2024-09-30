package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.EmptyCardHour
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HeartRateDetailEmptyScreen(navigation: NavHostController) {
    val heartData = List(24) { (1..50).random() }

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.my_heart_rate)
            )
        },
        content = {
                Column(
                    modifier = Modifier

                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyCardHour(
                        steps = heartData,
                        maxValue = 50,
                        barColor = Orange170,
                        accentColor = Rose120,
                        title = stringResource(id = R.string.heart_rate_card_title),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

        }
    )
}

@Preview
@Composable
fun HeartRateDetailEmptyPreview() {
    LogifitApppTheme {
        HeartRateDetailEmptyScreen(rememberNavController())
    }
}

@Preview
@Composable
fun HeartRateDetailEmptyDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        HeartRateDetailEmptyScreen(rememberNavController())
    }
}
