package com.example.logifitappp.viewmodel.views.LeaderBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.logifitappp.domain.repository.LeaderboardRepository
import com.example.logifitappp.viewmodel.views.LeaderboardViewModel

class LeaderboardViewModelFactory(private val repository: LeaderboardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}