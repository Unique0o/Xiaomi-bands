package com.example.logifitappp.domain.usecase

import com.example.logifitappp.data.remote.dto.response.NotificationResponse
import com.example.logifitappp.domain.service.NotificationService
import javax.inject.Inject

class NotificationUseCase @Inject constructor(private val notiService: NotificationService) {
    suspend operator fun invoke(): NotificationResponse {
        return notiService.store()
    }
}