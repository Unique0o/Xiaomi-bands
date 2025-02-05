package com.example.logifitappp.ui.screens.occupational_information

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.OccupationalInfoItemModel
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.occupationalInfo.OccupationalInfoItem
import com.example.logifitappp.ui.components.occupationalInfo.OccupationalInfoItemData
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoUiState
import com.example.logifitappp.viewmodel.views.OccupationalInformationViewModel
import java.util.Locale

data class OccupationItem(
    val id: Int?,
    val label: String?
)
val workLoadSuggestionsList = listOf(
    OccupationItem(
        0,
        "Hardly requires attention"
    ),
    OccupationItem(
        1,
        "Some of the time"
    ),
    OccupationItem(
        2,
        "Most of the time"
    ),
    OccupationItem(
        3,
        "Completely all of the time"
    ),
)

val occupationAttentions = listOf(
    OccupationItem(
        0,
        "Extremely undemanding, plenty of room for breaks"
    ),
    OccupationItem(
        1,
        " Low work load, some space for active breaks"
    ),
    OccupationItem(
        2,
        "Moderate workload, little space for active breaks"
    ),
    OccupationItem(
        3,
        "Extremely demanding, no space for active/passive break"
    ),
)
@Composable
fun OccupationalInformationView(
    navigation: NavHostController,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val viewModel: OccupationalInformationViewModel = hiltViewModel()
    val occupationalInfoState by viewModel.occupationalInfoState.collectAsState()
    val fieldToLocalValueMap = mutableMapOf(
        "Company" to "",
        "Group" to "",
        "Shift" to "",
    )
    LaunchedEffect(Unit) {
        appViewModel.apply {
            val company = tenant?.name
            val group = fetchUserGroup(tenant?.id?:0, user?.groupId?:0)
            val shift = fetchUserShift(tenant?.id?:0, user?.shiftId?:0)
            fieldToLocalValueMap["Company"] = company?:""
            fieldToLocalValueMap["Group"] = group?.name?:""
            fieldToLocalValueMap["Shift"] = shift?.name?:""
        }
    }

    SimplePage(
        topBar = {
            ColumnStackHeader(
                navigation = navigation,
                title = stringResource(id = R.string.occupational_info),
            )
        }
    ) {
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
                            data = OccupationalInfoItemData(
                                label = item.label,
                                value = obtainLabelValue(fieldToLocalValueMap, item),
                                suggestions = getRelevantSuggestions(item.label),
                                timerField = item.label.lowercase().contains("typical travel time")
                                ),
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
    }
}

fun obtainLabelValue(valueMap: MutableMap<String, String>, item: OccupationalInfoItemModel): String{
    return if(valueMap.contains(item.label)) {
        valueMap[item.label]?:""
    }else{
        item.value
    }
}

fun getRelevantSuggestions(label: String): List<OccupationItem>?{
    return if (label.lowercase().contains("work position")){
        workLoadSuggestionsList
    }else if(label.lowercase().contains("occupational attention type")){
        occupationAttentions
    }else{
        null
    }
}