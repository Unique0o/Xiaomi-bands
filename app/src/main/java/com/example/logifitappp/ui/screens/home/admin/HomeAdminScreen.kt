package com.example.logifitappp.ui.screens.home.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.IconButton
import com.example.logifitappp.ui.components.headers.CustomTopBar
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun HomeAdminScreen() {
    SimplePage(
        topBar = {
            CustomTopBar(
                title = stringResource(id = R.string.app_name),
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Watch,
                    contentDescription = "Smart Band",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.text_no_device),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(
                    icon = Icons.Default.Add,
                    onClick = { /* TODO */ },
                    text = stringResource(R.string.add_device)
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }

    )


}

@Composable
@Preview(showBackground = true)
fun HomeAdminScreenPreview() {
    LogifitApppTheme {
        HomeAdminScreen()
    }
}

@Composable
@Preview(showBackground = true)
fun HomeAdminScreenDarkPreview() {
    LogifitApppTheme(darkTheme = true) {
        HomeAdminScreen()
    }
}