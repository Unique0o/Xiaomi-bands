package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.requests.MarkAsCompletedLessonRequest
import com.example.logifitappp.domain.repository.LessonRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LessonService @Inject constructor(
    private val lessonRepository: LessonRepository
) {
    suspend fun all(trainingId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = lessonRepository.all(trainingId)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun find(lessonId: Int) = withContext(Dispatchers.IO) {
        try {
            val response = lessonRepository.find(lessonId)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun markAsCompleted(markAsCompletedLessonRequest: MarkAsCompletedLessonRequest) = withContext(Dispatchers.IO) {
        try {
            val response = lessonRepository.markAsCompleted(markAsCompletedLessonRequest)

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            val result = response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)

            return@withContext result.message != "La lección ya fue tomada."
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}