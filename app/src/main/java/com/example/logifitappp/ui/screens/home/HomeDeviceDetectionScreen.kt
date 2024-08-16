package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.BackHeader
import com.example.logifitappp.ui.components.home.Device.DeviceCard
import com.example.logifitappp.ui.components.home.Device.InfoMessage
import com.example.logifitappp.ui.components.home.Device.LoadingSpinner
import com.example.logifitappp.ui.components.modals.AuthenticationModal
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun DeviceDetectionScreen(
    navigation: NavHostController
) {
    var showAuthModal by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            navigation = navigation,
            title = stringResource(R.string.detecting_devices)
        )

        SimplePage(
            content = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        ) {
                            items(8) { index ->
                                DeviceCard(
                                    deviceName = "Mi Smart Band 4",
                                    deviceId = "DF:29:4A:30:2A:1C",
                                    onClick = {
                                        selectedDevice = "Dispositivo $index"
                                        showAuthModal = true
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
//                LoadingSpinner(
//                    text = stringResource(R.string.searching_for_devices),
//                    modifier = Modifier
//                        .padding(top = 130.dp)
//                )
//                    Spacer(modifier = Modifier.weight(1f))
                        InfoMessage(
                            icon = Icons.Filled.Info,
                            title = stringResource(R.string.make_your_device_detectable),
                            message = stringResource(R.string.device_detection_info),
                            modifier = Modifier
                                .padding(bottom = 36.dp)
                        )
                    }


                }
            },
        )

    }
    AuthenticationModal(
        isVisible = showAuthModal,
        onDismiss = { showAuthModal = false },
        onAuthenticate = { key ->
            println("Autenticando dispositivo: $selectedDevice con clave: $key")
            showAuthModal = true
            selectedDevice = null
        }
    )
}

@Preview
@Composable
fun DeviceDetectionScreenPreview() {
    LogifitApppTheme {
        DeviceDetectionScreen(rememberNavController())
    }
}

@Preview
@Composable
fun DeviceDetectionDarkScreenPreview() {
    LogifitApppTheme(darkTheme = true) {
        DeviceDetectionScreen(rememberNavController())
    }
}