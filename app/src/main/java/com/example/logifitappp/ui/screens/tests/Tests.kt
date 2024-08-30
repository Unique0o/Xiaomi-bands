package com.example.logifitappp.ui.screens.tests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.BackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.ui.components.graphics.CardLayout

@Composable
fun Tests( navigation: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            navigation = navigation,
            title = stringResource(id = R.string.settings)
        )
        SimplePage(
            content = {
                CardLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    bodyComponent = { /*TODO*/ },
                    icon = rememberVectorPainter(Icons.Default.Edit),
                    iconSize = 20.dp,
                    label = stringResource(id = R.string.subjective_test),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    cardBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    Textcolor = Color.Unspecified
                )
                CardLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    bodyComponent = { /*TODO*/ },
                    icon = rememberVectorPainter(Icons.Default.Edit),
                    iconSize = 20.dp,
                    label = stringResource(id = R.string.settings_2),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    cardBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                    Textcolor = Color.Unspecified
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    LogifitApppTheme {
        Tests(navigation = rememberNavController())
    }
}
@Preview(showBackground = true)
@Composable
fun TestDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        Tests(navigation = rememberNavController())
    }
}