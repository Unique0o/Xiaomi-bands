package com.example.logifitappp.ui.screens.healthInfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.HealthInfo.HealthInfoCard
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoUiState
import com.example.logifitappp.viewmodel.views.HealthInfo.HealthInfoViewModel

@Composable
fun HealthInfoScreen(
    navigation: NavHostController
) {
    val viewModel: HealthInfoViewModel = hiltViewModel()
    val health by viewModel.healthInfo.collectAsState()

    SimplePage(content = {
        when (health) {
            HealthInfoUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }
            is HealthInfoUiState.Success -> {
                val info = (health as HealthInfoUiState.Success).data
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(info.size) { index ->
                        val item = info[index]
                        HealthInfoCard(
                            title = item.title,
                            value = item.value,
                            unit = item.unit,
                            imageRes = item.imageRes,
                            onEditClick = { viewModel.onEditClick(/* */) }
                        )
                    }
                }
            }
            is HealthInfoUiState.Error -> {
                Text(
                    text = (health as HealthInfoUiState.Error).message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
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

