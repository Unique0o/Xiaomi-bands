package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.NotificationResponse
import retrofit2.Response

interface NotificationRepository {
    suspend fun getNotifications(): Response<NotificationResponse>
}