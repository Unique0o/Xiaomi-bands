package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.EvaluationApi
import com.example.logifitappp.data.remote.dto.response.EvaluationResultResponse
import com.example.logifitappp.domain.repository.EvaluationRepository
import retrofit2.Response
import javax.inject.Inject

class EvaluationRepositoryImpl @Inject constructor(
    private val evaluationApi: EvaluationApi
): EvaluationRepository {
    override suspend fun fetchResults(userId: Int): Response<List<EvaluationResultResponse>> {
        return evaluationApi.fetchResults(userId)
    }
}