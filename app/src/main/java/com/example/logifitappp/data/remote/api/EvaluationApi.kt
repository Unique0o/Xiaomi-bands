package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.StoreEvaluationRequest
import com.example.logifitappp.data.remote.dto.response.EvaluationResultResponse
import com.example.logifitappp.data.remote.dto.response.FetchEvaluationResponse
import com.example.logifitappp.data.remote.dto.response.FetchQuestionResponse
import com.example.logifitappp.data.remote.dto.response.StoreEvaluationResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EvaluationApi {
    @GET("api/tools_test")
    suspend fun all(): Response<List<FetchEvaluationResponse>>

    @GET("api/item_tools/{id}")
    suspend fun fetchQuestions(@Path("id") evaluationId: Int): Response<FetchQuestionResponse>

    @GET("api/result_user/{id}")
    suspend fun fetchResults(@Path("id") userId: Int): Response<List<EvaluationResultResponse>>

    @GET("api/result_tools/export/{id}")
    suspend fun fetchSpecificResult(@Path("id") evaluationId: Int): Response<ResponseBody>

    @POST("api/result_tools")
    suspend fun store(@Body storeEvaluationRequest: StoreEvaluationRequest): Response<StoreEvaluationResponse>
}