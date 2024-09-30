package com.example.logifitappp.ui.screens.graphics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.navigation.routes.MainRoutes
import com.example.logifitappp.ui.components.BottomNavigationBar
import com.example.logifitappp.ui.components.graphics.EmptyInfoGraph
import com.example.logifitappp.ui.components.graphics.StepEmptyGraphCard
import com.example.logifitappp.ui.components.headers.CardHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.components.titles.IconTitle
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.viewmodel.views.graphics.GraphicsViewModel

@Composable
fun GraphicsEmptyScreen(
    navigation: NavHostController,
    viewModel: GraphicsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CardHeader(
            user = viewModel.mockUsers[0],
            onNotificationClick = { },
            onProfileImageClick = {},
            bodyComponent = {}
        )
        Spacer(modifier = Modifier.height(8.dp))
        SimplePage(
            content = {

                Spacer(modifier = Modifier.height(16.dp))
                IconTitle(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_weather_night),
                    text = stringResource(id = R.string.my_sleep)
                )
                EmptyInfoGraph(
                    title = stringResource(id = R.string.information_between),
                    timeRange = "19:00 - 07:00",
                    indicatorInformation = stringResource(id = R.string.without_data),
                    icon = painterResource(id = R.drawable.ic_update)
                )
                Spacer(modifier = Modifier.height(16.dp))
                IconTitle(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_shoe_sneaker),
                    text = stringResource(id = R.string.my_steps)
                )
                val stepData = List(24) { kotlin.random.Random.nextFloat() }
                StepEmptyGraphCard(
                    data = stepData,
                    title = stringResource(id = R.string.kcal),
                    indicatorInformation= stringResource(id = R.string.without_data),
                    icon = painterResource(id = R.drawable.ic_fire),
                    barColor = Lime70
                )
                Spacer(modifier = Modifier.height(16.dp))

                IconTitle(
                    icon = ImageVector.vectorResource(id = R.drawable.ic_heart_pulse),
                    text = stringResource(id = R.string.my_heart_rate)
                )
                StepEmptyGraphCard(
                    title = stringResource(id = R.string.graph_card_heart_rate),
                    data = stepData,
                    icon = painterResource(id = R.drawable.ic_heart_cog),
                    barColor = Orange170,
                    indicatorInformation= stringResource(id = R.string.without_data),
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedRoute = MainRoutes.Graphics,
                    onRouteSelected = {
                        navigation.navigate(MainRoutes.Graphics)
                    }
                )
            }

        )
    }
}


@Preview(showBackground = true)
@Composable
fun GraphicsEmptyPreview() {
    LogifitApppTheme {
        GraphicsEmptyScreen(
            navigation = NavHostController(LocalContext.current),
            GraphicsViewModel()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GraphicsEmptyDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        GraphicsEmptyScreen(
            navigation = NavHostController(LocalContext.current),
            GraphicsViewModel()
        )
    }
}



