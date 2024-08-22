package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.HeartRateCard
import com.example.logifitappp.data.models.HeartRateData
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.home.CardItem
import com.example.logifitappp.ui.components.home.ConnectedIndicator
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
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_heart_pulse),
                    iconSize = 24.dp,
                    label = stringResource(id = R.string.average_heart_rate),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    style = Modifier.padding(8.dp),
                    suffixComponent = {
                        ConnectedIndicator(
                            text = "85 LPM",
                            color = Green298,
                            backgroundColor = Lime70,
                            pointColor = Green298
                        )
                    }
                )
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_heart_cog),
                    iconSize = 24.dp,
                    label = stringResource(id = R.string.graph_card_heart_rate),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    style = Modifier.padding(8.dp),
                    suffixComponent = {
                        ConnectedIndicator(
                            text = "90 LPM",
                            color = Green298,
                            backgroundColor = Lime70,
                            pointColor = Green298
                        )
                    }
                )

            }

        }
    )
}

@Preview
@Composable
fun HeartRateDetailPreview() {
    LogifitApppTheme {
        HeartRateDetailScreen(rememberNavController())
    }
}

@Preview
@Composable
fun HeartRateDarkModeDetailPreview() {
    LogifitApppTheme(darkTheme = true) {
        HeartRateDetailScreen(rememberNavController())
    }
}