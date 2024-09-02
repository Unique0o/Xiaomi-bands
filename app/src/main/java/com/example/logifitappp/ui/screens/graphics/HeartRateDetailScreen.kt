package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.HeartRateCard
import com.example.logifitappp.ui.components.graphics.HeartRateData
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.home.CardItem
import com.example.logifitappp.ui.components.titles.IconTitle
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HeartRateDetailScreen(navigation: NavHostController) {
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
                verticalArrangement = Arrangement.Top
            ) {
                val heartdata = HeartRateData(
                    date = "Noviembre 20, 2023",
                    minRate = 70,
                    maxRate = 101,
                    timeRange = "02:00 - 02:30",
                    ranges = listOf(
                        10 to 35,
                        25 to 45,
                        15 to 40,
                        20 to 40,
                        10 to 40,
                    )
                )
                HeartRateCard(heartdata)
                IconTitle(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_chart_box_outline),
                    text = stringResource(id = R.string.summary)
                )
                CardItem(
                    title = stringResource(id = R.string.average_heart_rate),
                    status = "85 LPM",
                    R.drawable.ic_heart_pulse,
                    statusColor = Green298,
                    backgroundColor = Lime70,
                    modifier = Modifier.padding()
                )
                CardItem(
                    title = stringResource(id = R.string.graph_card_heart_rate),
                    status = "90 LPM",
                    R.drawable.ic_heart_cog,
                    statusColor = Green298,
                    backgroundColor = Lime70,
                    modifier = Modifier.padding()
                )
            }

        }
    )
}

@Preview
@Composable
fun HeartRateDetailPreview(){
    LogifitApppTheme {
        HeartRateDetailScreen(rememberNavController())
    }
}

@Preview
@Composable
fun HeartRateDarkModeDetailPreview(){
    LogifitApppTheme(darkTheme = true) {
        HeartRateDetailScreen(rememberNavController())
    }
}