package com.example.logifitappp.di.services

import com.example.logifitappp.di.repositories.SleepReportRepository
import com.example.logifitappp.di.services.requests.SleepReportRequest
import com.example.logifitappp.di.services.responses.SleepReportResponse
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class SleepReportService @Inject constructor (private val repository: SleepReportRepository) {

    suspend fun fetchWeeklyReport(shiftIdentifier: Int?, groupIdentifier: Int?): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = repository.fetchWeeklyReport(shiftIdentifier, groupIdentifier)
                if (response.exists()) {
                    Result.success(response.absolutePath)
                } else {
                    deleteFile(response)
                    Result.failure(Exception(AppStatusCodeEnum.NO_INTERNET_CONNECTION.name))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun fetchReport(date: String, tenantId: String): SleepReportResponse = withContext(Dispatchers.IO) {
        try {
            val response = repository.fetchReport(SleepReportRequest(date, tenantId))
            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }
            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    private fun deleteFile(file: File) {
        if (file.exists()) {
            file.delete()
        }
    }
}