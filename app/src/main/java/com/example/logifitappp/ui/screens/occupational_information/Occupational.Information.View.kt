package com.example.logifitappp.ui.screens.occupational_information

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import com.example.logifitappp.R
import com.example.logifitappp.data.models.OccupationalInfoItemModel
import com.example.logifitappp.ui.components.BottomSheetSearchable
import com.example.logifitappp.ui.components.Text
import com.example.logifitappp.ui.components.headers.ColumnStackHeader
import com.example.logifitappp.ui.components.occupationalInfo.OccupationalInfoItem
import com.example.logifitappp.ui.components.pages.SimplePage
import com.example.logifitappp.utils.TimeAndDateUtils
import com.example.logifitappp.viewmodel.AppViewModel
import com.example.logifitappp.viewmodel.views.OccupationItem
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoUiState
import com.example.logifitappp.viewmodel.views.OccupationalInformationViewModel
import kotlinx.coroutines.launch

data class BottomSheetState(
    val isVisible: Boolean,
    val selectedItem: OccupationalInfoItemModel?
)

@Composable
fun OccupationalInformationView(
    navigation: NavHostController,
    appViewModel: AppViewModel = hiltViewModel()
) {
    val viewModel: OccupationalInformationViewModel = hiltViewModel()
    val occupationalInfoState by viewModel.occupationalInfoState.collectAsState()

    val selectableFields by lazy {
        listOf(
            "workload",
            "occupationalAttentionType"
        )
    }

    val selectedFieldIdMap = remember {
        mutableStateMapOf<String, OccupationItem?>(
            "workLoad" to null,
            "occupationalAttentionType" to null
        )
    }
//    val selectedSuggestion = remember { mutableStateOf<OccupationItem?>(null) }

    val isLoading = remember { mutableStateOf(false) }

    val data = remember { mutableStateListOf<OccupationalInfoItemModel>() }

    var selectableSheetState by remember { mutableStateOf(BottomSheetState(false, null)) }

    LaunchedEffect(occupationalInfoState) {
        when (occupationalInfoState) {
            is OccupationalInfoUiState.Loading -> {
                isLoading.value = true
            }

            is OccupationalInfoUiState.Success -> {
                isLoading.value = false
                data.addAll((occupationalInfoState as OccupationalInfoUiState.Success).data)
                appViewModel.apply {
                    val group = fetchUserGroup(tenant?.id ?: 0, user?.groupId ?: tenant?.id ?: 0)
                    val shift = fetchUserShift(tenant?.id ?: 0, user?.shiftId ?: 0)

                    fun updateField(id: String, value: String?) {
                        value?.takeIf { it.isNotBlank() }?.let { nonEmptyValue ->
                            data.find { it.id == id }?.value = nonEmptyValue
                        }
                    }

                    tenant?.let { updateField("companyName", it.name) }
                    group?.let { updateField("groupName", it.name) }
                    shift?.let { updateField("shift", it.name) }

                    user?.apply {
                        updateField("workPosition", workPosition)
                        updateField("function", functionName)
                        updateField("workload", workloadValue?.toString())
                        updateField("occupationalAttentionType", attentionValue?.toString())

                        commutingSeconds?.let {
                            updateField(
                                "timeTravel",
                                TimeAndDateUtils.formatSecondsToTime(it)
                            )
                        }
                        breakFrequencySeconds?.let {
                            updateField(
                                "breaksFrequency",
                                TimeAndDateUtils.formatSecondsToTime(it)
                            )
                        }
                        breakAverageSeconds?.let {
                            updateField(
                                "breaksLength",
                                TimeAndDateUtils.formatSecondsToTime(it)
                            )
                        }
                    }

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
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(data.size) { index ->
                    val occupationItem = data[index]/*if (data[index].id in selectableFields) data[index].copy(
                        value = selectedFieldIdMap[data[index].id]?.label
                            ?: stringResource(R.string.not_selected)
                    ) else data[index]*/
                    OccupationalInfoItem(
                        data = occupationItem,
                        onClick = {
                            if (occupationItem.id in selectableFields) {
                                if (selectableSheetState.selectedItem?.id != occupationItem.id) {
                                    selectableSheetState =
                                        BottomSheetState(true, occupationItem)
                                } else {
                                    selectableSheetState =
                                        selectableSheetState.copy(isVisible = true)
                                }

                            }
                        }
                    )
                }
            }
        }
        if (occupationalInfoState is OccupationalInfoUiState.Error) {
            Text(
                text = (occupationalInfoState as OccupationalInfoUiState.Error).message,
                color = Color.Red,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }

        val isBottomSheetVisible = selectableSheetState.isVisible
        if (isBottomSheetVisible) {
            val clickOccupationItem = selectableSheetState.selectedItem
            OptionBottomSheet(
                data = clickOccupationItem,
                suggestions = getRelevantSuggestions(clickOccupationItem?.id, viewModel),
                selectedItemId = selectedFieldIdMap[clickOccupationItem?.id]?.id,
                onDismiss = {
                    selectableSheetState = selectableSheetState.copy(isVisible = false, null)
                }
            ) { selectedDataItem ->
                selectableSheetState = selectableSheetState.copy(isVisible = false)
                selectableSheetState.selectedItem?.id?.apply {
                    if (selectedFieldIdMap[this]?.id != selectedDataItem.id) {
                        selectedFieldIdMap[this] = selectedDataItem
                        val index = data.indexOfFirst { item -> item.id == this }
                        if (index != -1) {
                            data[index] = data[index].copy(value = selectedDataItem.label ?: "")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionBottomSheet(
    modifier: Modifier = Modifier,
    data: OccupationalInfoItemModel?,
    suggestions: List<OccupationItem>?,
    selectedItemId: Int?,
    onDismiss: () -> Unit,
    onChangeValue: (OccupationItem) -> Unit
) {
    var isVisibleBottomSheetModal by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val updatedElements by rememberUpdatedState(suggestions)
    val selectedItem by remember {
        mutableStateOf(
            suggestions?.firstOrNull { item -> item.id == selectedItemId }
        )
    }

    val toggleModalBottomSheet = {
        coroutineScope.launch {
            if (isVisibleBottomSheetModal) modalBottomSheetState.hide()
            else modalBottomSheetState.show()
        }.invokeOnCompletion {
            isVisibleBottomSheetModal = !isVisibleBottomSheetModal
        }
    }

    LifecycleResumeEffect(Unit) {
        toggleModalBottomSheet()
        onPauseOrDispose { }
    }

    BottomSheetSearchable(
        coroutineScope = coroutineScope,
        elements = updatedElements ?: emptyList(),
        isVisible = isVisibleBottomSheetModal,
        modalBottomSheetState = modalBottomSheetState,
        onChange = onChangeValue,
        onDismissRequest = {
            isVisibleBottomSheetModal = false
            onDismiss()
        },
        title = data?.label ?: "",
        toggleModalBottomSheet = toggleModalBottomSheet,
        value = selectedItem,
    )
}

fun getRelevantSuggestions(
    id: String?,
    viewModel: OccupationalInformationViewModel
): List<OccupationItem>? {
    return when (id) {
        "workload" -> viewModel.workLoadSuggestionsList
        "occupationalAttentionType" -> viewModel.occupationAttentions
        else -> null
    }
}