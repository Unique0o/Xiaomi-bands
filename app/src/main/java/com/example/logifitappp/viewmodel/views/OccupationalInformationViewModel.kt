package com.example.logifitappp.viewmodel.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.GetOccupationalInfoUseCase
import com.example.logifitappp.ui.components.BottomSheetSelectableItem
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OccupationItem(
    override val id: Int,
    val label: String?
): BottomSheetSelectableItem(id)
@HiltViewModel
class OccupationalInformationViewModel @Inject constructor(
    private val getOccupationalInfoUseCase: GetOccupationalInfoUseCase
) : ViewModel() {
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

    private val _occupationalInfoState = MutableStateFlow<OccupationalInfoUiState>(
        OccupationalInfoUiState.Loading
    )
    val occupationalInfoState: StateFlow<OccupationalInfoUiState> = _occupationalInfoState.asStateFlow()

    init {
        loadOccupationalInfo()
    }

    private fun loadOccupationalInfo() {
        viewModelScope.launch {
            _occupationalInfoState.value = OccupationalInfoUiState.Loading
            try {
                val info = getOccupationalInfoUseCase()
                _occupationalInfoState.value = OccupationalInfoUiState.Success(info)
            } catch (e: Exception) {
                _occupationalInfoState.value =
                    OccupationalInfoUiState.Error("Failed to load occupational info")
            }
        }
    }
}

