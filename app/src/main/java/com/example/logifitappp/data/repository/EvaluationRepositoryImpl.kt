package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.EvaluationApi
import com.example.logifitappp.data.remote.dto.requests.StoreEvaluationRequest
import com.example.logifitappp.data.remote.dto.response.EvaluationResultResponse
import com.example.logifitappp.data.remote.dto.response.FetchEvaluationResponse
import com.example.logifitappp.data.remote.dto.response.FetchQuestionResponse
import com.example.logifitappp.data.remote.dto.response.StoreEvaluationResponse
import com.example.logifitappp.domain.repository.EvaluationRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class EvaluationRepositoryImpl @Inject constructor(
    private val evaluationApi: EvaluationApi
): EvaluationRepository {
    override suspend fun all(): Response<List<FetchEvaluationResponse>> {
        return evaluationApi.all()
    }

    override suspend fun fetchQuestions(evaluationId: Int): Response<FetchQuestionResponse> {
        return evaluationApi.fetchQuestions(evaluationId)
    }

    override suspend fun fetchResults(userId: Int): Response<List<EvaluationResultResponse>> {
        return evaluationApi.fetchResults(userId)
    }

    override suspend fun fetchSpecificResult(evaluationId: Int): Response<ResponseBody> {
        return evaluationApi.fetchSpecificResult(evaluationId)
    }

    override suspend fun store(storeEvaluationRequest: StoreEvaluationRequest): Response<StoreEvaluationResponse> {
        return evaluationApi.store(storeEvaluationRequest)
    }
}