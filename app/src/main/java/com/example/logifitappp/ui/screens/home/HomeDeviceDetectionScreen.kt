package com.example.logifitappp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.home.Device.InfoMessage
import com.example.logifitappp.ui.components.home.Device.LoadingSpinner
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.LogifitApppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetectionScreen(
    navigation: NavHostController
) {
    SimplePage(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 180.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingSpinner(
                    text = stringResource(R.string.searching_for_devices),
                    modifier = Modifier
                        .padding(top = 130.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                InfoMessage(
                    icon = Icons.Filled.Info,
                    title = stringResource(R.string.make_your_device_detectable),
                    message = stringResource(R.string.device_detection_info),
                    modifier = Modifier
                        .padding(bottom = 36.dp)
                )
            }


        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.detecting_devices),
                        textAlign = TextAlign.Center,
                        color = Blue690,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            Icons.Filled.ArrowBack, contentDescription = "Back",
                            tint = Blue690
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
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