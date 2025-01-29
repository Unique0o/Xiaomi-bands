package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.domain.repository.SynchronizationReportRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SynchronizationReportService @Inject constructor (private val repository: SynchronizationReportRepository) {
    suspend fun fetchReport(synchronizationReportRequest: SynchronizationReportRequest) = withContext(Dispatchers.IO) {
        try {
            val response = repository.fetchReport(synchronizationReportRequest)

            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}