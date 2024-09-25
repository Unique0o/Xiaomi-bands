package com.example.logifitappp.viewmodel.views

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App.Companion.context
import com.example.logifitappp.di.services.AuthService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    var username by mutableStateOf(TextFieldValue(""))
        private set

    var uiState by mutableStateOf(PasswordRecoveryUiState())
        private set

    fun updateUsername(username: TextFieldValue) {
        this.username = username
        uiState = uiState.copy(error = null)
    }

    fun recoverPassword() {
        if (!validateUsername()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val response = authService.recoverPassword(username.text)
                uiState = uiState.copy(
                    isLoading = false,
                    isSuccess = true,
                    successMessage = response.message
                )
            } catch (e: HttpConsumerException) {
                handleError(e)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
                Log.e("PasswordRecoveryViewModel", "Unexpected error", e)
            }
        }
    }

    private fun validateUsername(): Boolean {
        return if (username.text.isBlank()) {
            uiState = uiState.copy(error = context.getString(R.string.error_username_empty))
            false
        } else {
            true
        }
    }

    private fun handleError(e: HttpConsumerException) {
        val errorMessage = when (e.getStatus()) {
            AppStatusCodeEnum.UNREGISTERED_USER -> context.getString(R.string.unregistered_user_error_message)
            else -> context.getString(R.string.error_general)
        }
        uiState = uiState.copy(isLoading = false, error = errorMessage)
    }

    data class PasswordRecoveryUiState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val successMessage: String? = null,
        val error: String? = null
    )
}