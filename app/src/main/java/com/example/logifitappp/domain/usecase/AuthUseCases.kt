package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.remote.dto.requests.LoginRequest
import com.example.logifitappp.domain.service.AuthService
import javax.inject.Inject



class RecoverPasswordUseCase @Inject constructor(private val authService: AuthService) {
    suspend operator fun invoke(email: String) = authService.recoverPassword(email)
}

class UpdateNotificationTokenUseCase @Inject constructor(private val authService: AuthService) {
    suspend operator fun invoke(userId: Int, firebaseKey: String) =
        authService.updateNotificationToken(userId, firebaseKey)
}