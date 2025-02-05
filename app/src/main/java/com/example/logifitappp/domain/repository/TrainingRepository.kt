package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.FetchTrainingResponse
import okhttp3.ResponseBody
import retrofit2.Response

interface TrainingRepository {
    suspend fun all(): Response<List<FetchTrainingResponse>>

    suspend fun download(trainingId: Int): Response<ResponseBody>

    suspend fun find(trainingId: Int): Response<FetchTrainingResponse>
}