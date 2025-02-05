package com.example.logifitappp.domain.service

import com.example.logifitappp.domain.repository.NotificationRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationService  @Inject constructor (private val repository: NotificationRepository) {
    suspend fun all() = withContext(Dispatchers.IO) {
        try {
            val response = repository.getNotifications()

            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }

            return@withContext response.body()?.data ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}