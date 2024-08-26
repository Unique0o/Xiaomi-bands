package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.logifitappp.ui.components.home.ConnectedIndicator
//import com.example.logifitappp.ui.components.home.HeaderHome
import com.example.logifitappp.ui.components.home.OptionsList
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeDeviceDisconnectedScreen() {
    SimplePage(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
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
                            iconSize = 24.dp,
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

            }
        }
    )
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