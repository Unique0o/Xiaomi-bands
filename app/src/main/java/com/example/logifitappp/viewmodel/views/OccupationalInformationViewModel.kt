package com.example.logifitappp.viewmodel.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.data.User
import com.example.logifitappp.domain.usecase.GetOccupationalInfoUseCase
import com.example.logifitappp.data.models.OccupationalInfoItemModel
import com.example.logifitappp.viewmodel.views.OccupationalInfo.OccupationalInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OccupationalInformationViewModel @Inject constructor(
    private val getOccupationalInfoUseCase: GetOccupationalInfoUseCase
) : ViewModel() {

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

    private fun persistOccupationalInfo(user: User, occupationInfoList: List<OccupationalInfoItemModel>){

    }

    fun onItemClick(item: OccupationalInfoItemModel) {
    }
}

