package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.SleepReportRequest
import com.example.logifitappp.data.remote.dto.response.SleepReportResponse
import com.example.logifitappp.domain.repository.SleepReportRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SleepReportService @Inject constructor (private val repository: SleepReportRepository) {

    suspend fun fetchReport(sleepReportRequest: SleepReportRequest): SleepReportResponse = withContext(Dispatchers.IO) {
        try {
            val response = repository.fetchReport(sleepReportRequest)
            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }
            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}