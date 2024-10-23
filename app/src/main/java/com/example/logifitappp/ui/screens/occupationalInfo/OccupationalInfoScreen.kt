package com.example.logifitappp.ui.screens.occupationalInfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.occupationalInfo.OccupationalInfoItem
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoUiState
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoViewModel

@Composable
fun OccupationalInfoScreen(
    navigation: NavHostController,
) {
    val viewModel: OccupationalInfoViewModel = hiltViewModel()
    val occupationalInfoState by viewModel.occupationalInfoState.collectAsState()

    SimplePage(
        content = {
            when (occupationalInfoState) {
                is OccupationalInfoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                is OccupationalInfoUiState.Success -> {
                    val info = (occupationalInfoState as OccupationalInfoUiState.Success).data

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(info.size) { index ->
                            val item = info[index]
                            OccupationalInfoItem(
                                label = item.label,
                                value = item.value,
                                onClick = { viewModel.onItemClick(item) }
                            )
                        }
                    }
                }
                is OccupationalInfoUiState.Error -> {
                    Text(
                        text = (occupationalInfoState as OccupationalInfoUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.occupational_info),
            )
        }
    )
}

@Composable
@Preview
fun OccupationalInfoScreenPreview() {
    LogifitApppTheme {
        OccupationalInfoScreen(
            navigation = rememberNavController()
        )
    }
}