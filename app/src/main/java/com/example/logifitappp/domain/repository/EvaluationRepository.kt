package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.StoreEvaluationRequest
import com.example.logifitappp.data.remote.dto.response.EvaluationResultResponse
import com.example.logifitappp.data.remote.dto.response.FetchEvaluationResponse
import com.example.logifitappp.data.remote.dto.response.FetchQuestionResponse
import com.example.logifitappp.data.remote.dto.response.StoreEvaluationResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface EvaluationRepository {
    suspend fun all(): Response<List<FetchEvaluationResponse>>

    suspend fun fetchQuestions(evaluationId: Int): Response<FetchQuestionResponse>

    suspend fun fetchResults(userId: Int): Response<List<EvaluationResultResponse>>

    suspend fun fetchSpecificResult(evaluationId: Int): Response<ResponseBody>

    suspend fun store(storeEvaluationRequest: StoreEvaluationRequest): Response<StoreEvaluationResponse>
}