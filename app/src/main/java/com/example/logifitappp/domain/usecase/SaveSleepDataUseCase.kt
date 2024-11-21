package com.example.logifitappp.domain.usecase


import com.example.logifitappp.data.remote.dto.requests.SleepWrittenDataRequest
import com.example.logifitappp.domain.service.SleepWrittenDataService
import javax.inject.Inject

class SaveSleepDataUseCase @Inject constructor(
    private val sleepDataService: SleepWrittenDataService
) {
    suspend operator fun invoke(request: SleepWrittenDataRequest): Result<Unit> {
        return try {
            sleepDataService.saveSleepRecord(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}