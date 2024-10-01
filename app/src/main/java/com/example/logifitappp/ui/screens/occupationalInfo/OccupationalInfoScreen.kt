package com.example.logifitappp.ui.screens.occupationalInfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.occupationalInfo.OccupationalInfoItem
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.ui.theme.LogifitApppTheme

@Composable
fun OccupationalInfoScreen(
    onBackClick: () -> Unit,
    occupationalInfo: List<OccupationalInfoItem>,
    navigation: NavHostController
) {
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
                        onClick = { /* Handle item click */ }
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
            onBackClick = {},
            occupationalInfo = previewInfo,
            navigation = rememberNavController()
        )
    }
}