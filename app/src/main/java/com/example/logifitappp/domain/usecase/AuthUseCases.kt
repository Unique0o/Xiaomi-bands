package com.example.logifitappp.domain.usecase

import com.example.logifitappp.domain.service.AuthService
import javax.inject.Inject


class UpdateNotificationTokenUseCase @Inject constructor(private val authService: AuthService) {
    suspend operator fun invoke(userId: Int, firebaseKey: String) =
        authService.updateNotificationToken(userId, firebaseKey)
}