package com.example.logifitappp.viewmodel.views

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.di.services.AuthService
import com.example.logifitappp.di.services.requests.LoginRequest
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.views.Authentication.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService

) : ViewModel() {
    var isLogin by mutableStateOf(false)
        private set

    var password by mutableStateOf(TextFieldValue(""))
        private set

    var username by mutableStateOf(TextFieldValue(""))
        private set

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    fun login(onLoginSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val user = authService.login(LoginRequest(username.text, password.text))
                Log.d("LoginViewModel", "User logged in. Role: ${user.role}")
                val adminStatus = user.isAdmin()
                _isAdmin.value = adminStatus
                Log.d("LoginViewModel", "Is admin: $adminStatus")
                uiState = uiState.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    user = user
                )
                onLoginSuccess(adminStatus)
            } catch (e: HttpConsumerException) {
                val errorMessage = when (e.getStatus()) {
                    AppStatusCodeEnum.PASSWORD_NOT_VALIDATED,
                    AppStatusCodeEnum.UNREGISTERED_USER -> "Usuario o contraseña incorrectos"
                    else -> "Ha ocurrido un error. Por favor, intenta nuevamente más tarde."
                }
                uiState = uiState.copy(
                    isLoading = false,
                    error = errorMessage
                )
            }
        }
    }

    fun updatePassword(password: TextFieldValue) {
        this.password = password
    }

    fun updateUsername(username: TextFieldValue) {
        this.username = username
    }
}