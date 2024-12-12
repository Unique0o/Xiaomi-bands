package com.example.logifitappp.domain.service


import com.example.logifitappp.data.remote.dto.requests.SleepWrittenDataRequest
import com.example.logifitappp.domain.repository.SleepWrittenDataRepository
import javax.inject.Inject


class SleepWrittenDataService @Inject constructor(
    private val repository: SleepWrittenDataRepository
) {
    suspend fun saveSleepRecord(request: SleepWrittenDataRequest): Result<Unit> {
        return try {
            repository.saveSleepData(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}