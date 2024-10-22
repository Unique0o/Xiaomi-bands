package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.domain.service.AuthService
import javax.inject.Inject

class UpdateNotificationToken @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(userId: Int) {
        val firebaseToken = App.preferences.getString(AppPreferences.FIREBASE_NOTIFICATION_TOKEN, "")
        authService.updateNotificationToken(userId, firebaseToken)
    }
}