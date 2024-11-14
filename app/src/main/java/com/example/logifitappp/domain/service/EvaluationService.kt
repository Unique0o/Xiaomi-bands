package com.example.logifitappp.domain.service

import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.EvaluationResultModel
import com.example.logifitappp.data.remote.dto.response.toEvaluationResultModel
import com.example.logifitappp.domain.repository.EvaluationRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class EvaluationService @Inject constructor(private val evaluationRepository: EvaluationRepository) {
    suspend fun fetchResults(): List<EvaluationResultModel> = withContext(Dispatchers.IO) {
        try {
            val user = App.database.userDao().getLoggedIn()
            require(user != null) { println("User is required") }

            val response = evaluationRepository.fetchResults(user.id)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            val body = response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext body.map { it.toEvaluationResultModel(user.id) }
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchSpecificResult(evaluationId: Int): ResponseBody = withContext(Dispatchers.IO) {
        try {
            val response = evaluationRepository.fetchSpecificResult(evaluationId)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}