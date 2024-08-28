package com.example.logifitappp.viewmodel.views.LeaderBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.data.User
import com.example.logifitappp.domain.repository.LeaderboardRepository
import com.example.logifitappp.ui.components.ranking.LeaderboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class LeaderboardViewModel(
    private val repository: LeaderboardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        fetchLeaderboard()
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val allUsers = repository.getLeaderboard()
                _uiState.value = _uiState.value.copy(
                    topThreeUsers = allUsers.take(3),
                    nextThreeUsers = allUsers.drop(3).take(3),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}