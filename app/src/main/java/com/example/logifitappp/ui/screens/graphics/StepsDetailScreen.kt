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
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.graphics.StepCard
import com.example.logifitappp.ui.components.home.CardItem
import com.example.logifitappp.ui.components.titles.IconTitle
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun StepsDetailScreen(navigation: NavHostController) {
    val stepData = List(24) { (1..50).random() }

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.my_steps)
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                val steps = listOf(10, 20, 15, 30, 25, 35, 40)
                StepCard(steps= steps)
                IconTitle(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_chart_box_outline),
                    text = stringResource(id = R.string.summary)
                )
                CardItem(
                    title = stringResource(id = R.string.calories_burned),
                    status = "200 KCAL",
                    R.drawable.ic_fire,
                    statusColor = Green298,
                    backgroundColor = Lime70,
                    modifier = Modifier.padding()
                )
                CardItem(
                    title = stringResource(id = R.string.distance_traveled),
                    status = "1.5 km",
                    R.drawable.ic_road_variant,
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
fun StepsDetailPreview(){
    LogifitApppTheme(darkTheme = true) {
        StepsDetailScreen(rememberNavController())
    }
}