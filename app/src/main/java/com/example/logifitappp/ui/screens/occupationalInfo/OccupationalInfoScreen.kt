package com.example.logifitappp.ui.screens.occupationalInfo

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.occupationalInfo.OccupationalInfoItem
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoViewModel
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoViewModelFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent

@Composable
fun OccupationalInfoScreen(
    navigation: NavHostController,
) {
    val context = LocalContext.current
    val viewModel: OccupationalInfoViewModel = viewModel(
        factory = EntryPointAccessors.fromActivity(
            context as Activity,
            ViewModelFactoryProvider::class.java
        ).occupationalInfoViewModelFactory()
    )

    val occupationalInfo by viewModel.occupationalInfo.collectAsState()

    SimplePage(
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(occupationalInfo.size) { index ->
                    val item = occupationalInfo[index]
                    OccupationalInfoItem(
                        label = item.label,
                        value = item.value,
                        onClick = { viewModel.onItemClick(item) }
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


data class OccupationalInfoItem(
    val label: String,
    val value: String
)
@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    fun occupationalInfoViewModelFactory(): OccupationalInfoViewModelFactory
}

@Composable
@Preview
fun OccupationalInfoScreenPreview() {
    val previewInfo = listOf(
        OccupationalInfoItem(stringResource(R.string.company), "LOGIFIT"),
        OccupationalInfoItem(stringResource(R.string.group), stringResource(R.string.not_assigned)),
        OccupationalInfoItem(stringResource(R.string.shift), stringResource(R.string.not_selected)),
        OccupationalInfoItem(stringResource(R.string.work_position), stringResource(R.string.not_assigned)),
        OccupationalInfoItem(stringResource(R.string.function), stringResource(R.string.not_assigned)),
        OccupationalInfoItem(stringResource(R.string.travel_time), stringResource(R.string.not_selected)),
        OccupationalInfoItem(stringResource(R.string.workload), stringResource(R.string.not_selected)),
        OccupationalInfoItem(stringResource(R.string.occupational_attention_type), stringResource(R.string.not_selected)),
        OccupationalInfoItem(stringResource(R.string.breaks_frequency), stringResource(R.string.not_selected)),
        OccupationalInfoItem(stringResource(R.string.breaks_length), stringResource(R.string.not_selected))
    )

    LogifitApppTheme {
        OccupationalInfoScreen(
            navigation = rememberNavController()
        )
    }
}