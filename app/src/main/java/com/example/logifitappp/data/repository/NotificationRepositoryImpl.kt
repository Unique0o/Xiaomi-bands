package com.example.logifitappp.data.repository


import com.example.logifitappp.data.remote.api.NotificationApi
import com.example.logifitappp.data.remote.dto.response.NotificationResponse
import com.example.logifitappp.domain.repository.NotificationRepository
import retrofit2.Response
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(private val notiApi: NotificationApi)
    : NotificationRepository {

    override suspend fun getNotifications(): Response<NotificationResponse> {
        return notiApi.getNotifications()
    }
}