package com.example.logifitappp.viewmodel.views.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _shouldNavigateToMain = MutableStateFlow(false)
    val shouldNavigateToMain: StateFlow<Boolean> = _shouldNavigateToMain.asStateFlow()

    init {
        viewModelScope.launch {
            _isAdmin.value = userRepository.isUserAdmin()
        }
    }

    fun onSkipClicked() {
        _shouldNavigateToMain.value = true
    }

    fun onNavigationHandled() {
        _shouldNavigateToMain.value = false
    }

    fun activateBluetooth() {
        _shouldNavigateToMain.value = true
    }
}

interface UserRepository {
    suspend fun isUserAdmin(): Boolean
}