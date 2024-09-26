package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.domain.service.AuthService
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(private val authService: AuthService) {
    suspend operator fun invoke(email: String): GeneralResponse {
        return authService.recoverPassword(email)
    }
}