package com.example.logifitappp.viewmodel.views.LeaderBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.data.LeaderboardRepository
import com.example.logifitappp.data.User
import com.example.logifitappp.ui.components.ranking.LeaderboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class LeaderboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        fetchLeaderboard()
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
               
                val allUsers  = listOf(
                    User("User 1", 300, R.drawable.user1, true, 1),
                    User("User 2", 200, R.drawable.user1, false, 2),
                    User("User 3", 100, R.drawable.user1, false, 3),
                    User("RICARDO LOPEZ HUAMAN", 110, R.drawable.user1, false, 4),
                    User("CARLOS EDUARDO TORRES ZARATE", 105, R.drawable.user1, false, 5),
                    User("User 6", 90, R.drawable.user1, false, 6)
                )
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