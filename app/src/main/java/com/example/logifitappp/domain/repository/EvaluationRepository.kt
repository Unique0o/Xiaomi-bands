package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.EvaluationResultResponse
import retrofit2.Response

interface EvaluationRepository {
    suspend fun fetchResults(userId: Int): Response<List<EvaluationResultResponse>>
}