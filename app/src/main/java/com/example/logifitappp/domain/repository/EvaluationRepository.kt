package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.EvaluationResultResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface EvaluationRepository {
    suspend fun fetchResults(userId: Int): Response<List<EvaluationResultResponse>>

    suspend fun fetchSpecificResult(evaluationId: Int): Response<ResponseBody>
}