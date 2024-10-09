package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.TermsAndConditionsApi
import com.example.logifitappp.data.remote.dto.response.TermsAndConditionsResponse
import com.example.logifitappp.domain.repository.TermsAndConditionsRepository
import javax.inject.Inject

class TermsAndConditionsRepositoryImpl @Inject constructor(
    private val api: TermsAndConditionsApi
) : TermsAndConditionsRepository {
    override suspend fun getTermsAndConditions(): Result<TermsAndConditionsResponse> {
        return try {
            val response = api.getTermsAndConditions()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}