package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.SleepWrittenDataRequest


interface SleepWrittenDataRepository {
    suspend fun saveSleepData(request: SleepWrittenDataRequest): Result<Unit>
}