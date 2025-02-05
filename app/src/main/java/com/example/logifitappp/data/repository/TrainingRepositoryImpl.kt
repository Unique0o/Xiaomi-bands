package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.TrainingApi
import com.example.logifitappp.data.remote.dto.response.FetchTrainingResponse
import com.example.logifitappp.domain.repository.TrainingRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val trainingApi: TrainingApi
) : TrainingRepository {
    override suspend fun all(): Response<List<FetchTrainingResponse>> {
        return trainingApi.all()
    }

    override suspend fun download(trainingId: Int): Response<ResponseBody> {
        return trainingApi.download(trainingId)
    }

    override suspend fun find(trainingId: Int): Response<FetchTrainingResponse> {
        return trainingApi.find(trainingId)
    }
}