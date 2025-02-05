package com.example.logifitappp.domain.service

import com.example.logifitappp.domain.repository.AdminRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AdminService @Inject constructor(private val adminRepository: AdminRepository) {
    suspend fun fetchWorkers() = withContext(Dispatchers.IO) {
        try {
            val response = adminRepository.fetchWorkers()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            val result = response.body()
                ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
            val workers = result.data

            if (workers.isEmpty()) throw HttpConsumerException(AppStatusCodeEnum.WORKER_LIST_EMPTY)

            return@withContext workers
        } catch (e: HttpConsumerException) {
            throw e
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}