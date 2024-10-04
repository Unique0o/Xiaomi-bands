package com.example.logifitappp.viewmodel.views.OccupationalInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.domain.repository.OccupationalInfoRepository
import com.example.logifitappp.ui.screens.occupationalInfo.OccupationalInfoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OccupationalInfoViewModel @Inject constructor(
    private val repository: OccupationalInfoRepository
) : ViewModel() {

    private val _occupationalInfo = MutableStateFlow<List<OccupationalInfoItem>>(emptyList())
    val occupationalInfo: StateFlow<List<OccupationalInfoItem>> = _occupationalInfo

    init {
        loadOccupationalInfo()
    }

    private fun loadOccupationalInfo() {
        viewModelScope.launch {
            try {
                _occupationalInfo.value = repository.getOccupationalInfo()
            } catch (e: Exception) {
                _occupationalInfo.value = emptyList()
            }
        }
    }

    fun onItemClick(item: OccupationalInfoItem) {
    }

    sealed class OccupationalInfoState {
        object Loading : OccupationalInfoState()
        data class Success(val data: List<OccupationalInfoItem>) : OccupationalInfoState()
        data class Error(val message: String) : OccupationalInfoState()
    }
}
class OccupationalInfoViewModelFactory @Inject constructor(
    private val repository: OccupationalInfoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OccupationalInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OccupationalInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}