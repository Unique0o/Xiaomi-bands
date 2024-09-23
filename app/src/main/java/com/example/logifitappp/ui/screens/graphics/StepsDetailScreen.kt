package com.example.logifitappp.ui.screens.graphics


import androidx.compose.foundation.layout.padding
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
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.components.graphics.StepCard
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.titles.IconTitle
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun StepsDetailScreen(navigation: NavHostController) {
    val steps = listOf(10, 20, 15, 30, 25, 35, 40)

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.my_steps)
            )
        },
        content = {
            StepCard(steps= steps)
            IconTitle(
                icon = ImageVector.vectorResource(id = R.drawable.ic_chart_box_outline),
                text = stringResource(id = R.string.summary)
            )
            CardLayout(bodyComponent = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_fire),
                iconSize = 24.dp,
                label = stringResource(id = R.string.calories_burned),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                style = Modifier.padding(2.dp),
                suffixComponent = {
                    ConnectedIndicator(
                        text = "200 KCAL",
                        color = Green298,
                        backgroundColor = Lime70,
                        pointColor = Green298
                    )
                }
            )
            CardLayout(bodyComponent = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_road_variant),
                iconSize = 24.dp,
                label = stringResource(id = R.string.distance_traveled),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                style = Modifier.padding(2.dp),
                suffixComponent = {
                    ConnectedIndicator(
                        text = "1.5 km",
                        color = Green298,
                        backgroundColor = Lime70,
                        pointColor = Green298
                    )
                }
            )
        }
    )
}


@Preview
@Composable
fun StepsDetailPreview(){
    LogifitApppTheme {
        StepsDetailScreen(rememberNavController())
    }
}

@Preview
@Composable
fun StepsDetailDarkModePreview(){
    LogifitApppTheme(darkTheme = true) {
        StepsDetailScreen(rememberNavController())
    }
}