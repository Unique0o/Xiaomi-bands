package com.example.logifitappp.viewmodel.views.Authentication

import com.example.logifitappp.data.models.UserModel

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: UserModel? = null,
    val error: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null
)