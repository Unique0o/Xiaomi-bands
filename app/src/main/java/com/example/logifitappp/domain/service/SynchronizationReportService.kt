package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.domain.repository.SynchronizationReportRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SynchronizationReportService @Inject constructor(
    private val synchronizationReportRepository: SynchronizationReportRepository
) {
    suspend fun download(tenantId: Int, shiftId: Int, groupId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = synchronizationReportRepository.download(2, tenantId, if (shiftId == 0) null else shiftId, if (groupId == 0) null else groupId)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchReport(synchronizationReportRequest: SynchronizationReportRequest) = withContext(Dispatchers.IO) {
        try {
            val response = synchronizationReportRepository.fetchReport(synchronizationReportRequest)

            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}