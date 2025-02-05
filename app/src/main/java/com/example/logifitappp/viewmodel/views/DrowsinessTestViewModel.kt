package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.data.remote.dto.response.FetchEvaluationResponse
import com.example.logifitappp.domain.service.EvaluationService
import com.example.logifitappp.viewmodel.states.DrowsinessTestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrowsinessTestViewModel @Inject constructor(
    private val evaluationService: EvaluationService
): ViewModel() {
    var state by mutableStateOf(DrowsinessTestState())
        private set

    var tests = mutableStateListOf<FetchEvaluationResponse>()
        private set

    init {
        fetchTests()
    }

    fun fetchTests() {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                tests.addAll(evaluationService.all())

                state = state.copy(hasFetchTestsFailed = false)
            } catch (e: Exception) {
                state = state.copy(hasFetchTestsFailed = true)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}