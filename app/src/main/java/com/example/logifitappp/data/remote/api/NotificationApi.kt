package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {
    @GET("api/users/me/notifications")
    suspend fun getNotifications(): Response<NotificationResponse>
}