package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.logifitappp.ui.components.home.ConnectedIndicator
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeDeviceDisconnectedScreen() {
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
                CardItemButton(
                    title = stringResource(id = R.string.title_smart_band),
                    description = stringResource(id = R.string.card_description),
                    icon = R.drawable.ic_watch,
                    iconButton = R.drawable.ic_bluetooth,
                    buttonColor = Blue690,
                    onClick = { /*TODO*/ }
                )
                CardItemButton(
                    title = stringResource(id = R.string.title_my_test),
                    description = stringResource(id = R.string.card_description_message),
                    icon = R.drawable.ic_test,
                    iconButton = R.drawable.ic_add,
                    buttonColor = Blue690,
                    onClick = { /*TODO*/ }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDeviceDisconnectedScreenPreview() {
    LogifitApppTheme {
        HomeDeviceDisconnectedScreen()

    }
}

@Preview(showBackground = true)
@Composable
fun HomeDeviceDisconnectedScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        HomeDeviceDisconnectedScreen()

    }
}