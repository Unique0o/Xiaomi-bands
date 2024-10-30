package com.example.logifitappp.ui.screens.tests

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.graphics.CardLayout
import com.example.logifitappp.ui.components.headers.BackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun ResultFatigueSelfAssessmentTestScreen(
    isApt: Boolean,
    navigation: NavHostController,
    date: String
) {
    SimplePage(
        topBar = {
            BackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.result)
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.result_date) +" "+ date,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isApt) {
                    CardLayout(
                        bodyComponent = { /*TODO*/ },
                        label = stringResource(id = R.string.user_can_perform),
                        textcolor = MaterialTheme.colorScheme.inverseSurface,
                        cardBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                        elevation= CardDefaults.cardElevation(defaultElevation = 1.dp)
                    )
                } else {
                    CardLayout(
                        bodyComponent = { /*TODO*/ },
                        label = stringResource(id = R.string.no_drive),
                        textcolor = MaterialTheme.colorScheme.inverseSurface,
                        cardBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                        elevation= CardDefaults.cardElevation(defaultElevation = 1.dp)
                    )
                    CardLayout(
                        bodyComponent = { /*TODO*/ },
                        label = stringResource(id = R.string.medical_evaluation),
                        textcolor = MaterialTheme.colorScheme.inverseSurface,
                        cardBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                        elevation= CardDefaults.cardElevation(defaultElevation = 1.dp)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun ResultFatigueSelfAssessmentTestScreenPreview() {
    LogifitApppTheme {
        ResultFatigueSelfAssessmentTestScreen(isApt = true, navigation = rememberNavController(), date = "12/10/2023")
    }
}

@Preview
@Composable
fun ResultFatigueSelfAssessmentTestScreenDarkModePreview() {
    LogifitApppTheme(darkTheme = true) {
        ResultFatigueSelfAssessmentTestScreen(isApt = false, navigation = rememberNavController(), date = "12/10/2023")
    }
}