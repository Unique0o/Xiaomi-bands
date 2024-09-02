package com.example.logifitappp.ui.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.headers.UserProfileCard
import com.example.logifitappp.ui.components.home.CardItemButton
import com.example.logifitappp.ui.components.home.CardTest
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.home.FatigueTestItem
import com.example.logifitappp.ui.components.home.TitleSmartBandHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeTestScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        UserProfileCard(
            userName = "MARIA MERCEDES",
            userType = "PREMIUM",
            profileImageRes = R.drawable.user1,
            onNotificationClick = { },
            plan = "PREMIUM",
            bodyComponent = {
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_clock),
                    iconSize = 20.dp,
                    label = stringResource(id = R.string.schedule),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,

                    suffixComponent = {
                        ConnectedIndicator(
                            text = stringResource(id = R.string.status),
                            color = Green298,
                            backgroundColor = Lime70,
                            pointColor = Green298
                        )
                    }
                )
                CardLayout(bodyComponent = { /*TODO*/ },
                    icon = painterResource(id = R.drawable.ic_location),
                    iconSize = 24.dp,
                    label = stringResource(id = R.string.my_location),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    suffixComponent = {
                        ConnectedIndicator(
                            text = stringResource(id = R.string.PGT),
                            color = Green298,
                            backgroundColor = Lime70,
                            pointColor = Green298
                        )
                    }
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        SimplePage(
            content = {
                Spacer(modifier = Modifier.height(8.dp))
                TitleSmartBandHeader()
                CardTest()
                CardItemButton(
                    title = stringResource(id = R.string.title_my_test),
                    description = stringResource(id = R.string.card_description_message),
                    icon = R.drawable.ic_test,
                    iconButton = R.drawable.ic_add,
                    buttonColor = Green298,
                    onClick = { /*TODO*/ }
                )
                FatigueTestItem(date = "24/10/2023")

            }
        )
    }


}

@Preview(showBackground = true)
@Composable
fun HomeTestScreenPreview() {
    LogifitApppTheme {
        HomeTestScreen()

    }
}

@Preview(showBackground = true)
@Composable
fun HomeTestDarkModeScreenPreview() {
    LogifitApppTheme(darkTheme = true) {
        HomeTestScreen()

    }
}