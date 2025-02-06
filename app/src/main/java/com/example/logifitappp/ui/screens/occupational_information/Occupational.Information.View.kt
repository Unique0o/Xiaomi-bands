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
import com.example.logifitappp.viewmodel.views.OccupationItem
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoUiState
import com.example.logifitappp.viewmodel.views.OccupationalInformationViewModel
@Composable
fun OccupationalInformationView(
    navigation: NavHostController,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val viewModel: OccupationalInformationViewModel = hiltViewModel()
    val occupationalInfoState by viewModel.occupationalInfoState.collectAsState()


    val isLoading = remember { mutableStateOf(false) }

    val data = remember { mutableStateListOf<OccupationalInfoItemModel>() }

    LaunchedEffect(occupationalInfoState) {
        when (occupationalInfoState) {
            is OccupationalInfoUiState.Loading -> {
                isLoading.value = true
            }
            is OccupationalInfoUiState.Success -> {
                isLoading.value = false
                data.addAll((occupationalInfoState as OccupationalInfoUiState.Success).data)
                appViewModel.apply {
                    val company = tenant?.name
                    val group = fetchUserGroup(tenant?.id?:0, user?.groupId?:tenant?.id?:0)
                    val shift = fetchUserShift(tenant?.id?:0, user?.shiftId?:0)
                    data.find { it.id == "companyName" }?.let { it.value = company ?: "" }
                    data.find { it.id == "groupName" }?.let { it.value = group?.name ?: "" }
                    data.find { it.id == "shift" }?.let { it.value = shift?.name ?: "" }
                }
            }
            is OccupationalInfoUiState.Error -> {
                isLoading.value = false
            }
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
        if (isLoading.value){
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(data.size) { index ->
                    val occupationItem = data[index]
                    OccupationalInfoItem(
                        data = OccupationalInfoItemData(
                            label = occupationItem.label,
                            value = occupationItem.value,
                            suggestions = getRelevantSuggestions(occupationItem.id, viewModel),
                            timerField = occupationItem.id == "timeTravel"
                        ),
                        onClick = { viewModel.onItemClick(occupationItem) }
                    )
                }
            }
        }
        if(occupationalInfoState is OccupationalInfoUiState.Error) {
            Text(
                text = (occupationalInfoState as OccupationalInfoUiState.Error).message,
                color = Color.Red,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun getRelevantSuggestions(id: String?, viewModel: OccupationalInformationViewModel): List<OccupationItem>?{
    return when(id){
        "workload" -> viewModel.workLoadSuggestionsList
        "occupationalAttentionType" -> viewModel.occupationAttentions
        else -> null
    }
}