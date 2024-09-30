package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.response.WorkerResponse
import com.example.logifitappp.domain.repository.AdminRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdminService @Inject constructor (private val repository: AdminRepository) {
    suspend fun fetchWorkers(): WorkerResponse = withContext(
        Dispatchers.IO) {
        try {
            val response = repository.fetchWorkers()
            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }
            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}