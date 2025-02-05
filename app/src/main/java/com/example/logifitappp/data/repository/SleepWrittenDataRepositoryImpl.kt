package com.example.logifitappp.data.repository


import com.example.logifitappp.data.remote.api.SleepWrittenDataApi
import com.example.logifitappp.data.remote.dto.requests.SleepWrittenDataRequest
import com.example.logifitappp.domain.repository.SleepWrittenDataRepository
import javax.inject.Inject
import retrofit2.Response

class SleepWrittenDataRepositoryImpl @Inject constructor(
    private val api: SleepWrittenDataApi
) : SleepWrittenDataRepository {
    override suspend fun saveSleepData(request: SleepWrittenDataRequest): Result<Unit> {
        return api.saveSleepRecord(request)
    }
}
