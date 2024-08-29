package com.example.logifitappp.ui.components.ranking

import com.example.logifitappp.data.User


data class LeaderboardUiState(
    val topThreeUsers: List<User> = emptyList(),
    val nextThreeUsers: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)