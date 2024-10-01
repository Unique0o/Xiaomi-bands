package com.example.logifitappp.ui.screens.healthInfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.HealthInfo.HealthInfoCard
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

data class HealthInfo(
    val title: String,
    val value: String,
    val unit: String,
    val imageRes: Int
)

@Composable
fun HealthInfoScreen(
    navigation: NavHostController,
    healthInfo: List<HealthInfo>,
    onEditClick: (String) -> Unit
) {
    SimplePage(content = {
        Column(modifier = Modifier.fillMaxSize()) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(healthInfo.size) { index ->
                    val info = healthInfo[index]
                    HealthInfoCard(
                        title = info.title,
                        value = info.value,
                        unit = info.unit,
                        imageRes = info.imageRes,
                        onEditClick = { onEditClick(info.title) }
                    )
                }
            }
        }
    },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.health_info)
            )
        }

    )

}

@Composable
@Preview
fun HealthInfoScreenPreview() {
    LogifitApppTheme {

        val previewHealthInfo = listOf(
            HealthInfo("Weight", "-", "kg", R.drawable.user1),
            HealthInfo("Height", "-", "meters", R.drawable.user1),
            HealthInfo("Blood type", "B-", "", R.drawable.user1),
            HealthInfo("Gender", "male", "", R.drawable.user1)
        )

        HealthInfoScreen(
            navigation = rememberNavController(),
            healthInfo = previewHealthInfo,
            onEditClick = {}
        )
    }

}