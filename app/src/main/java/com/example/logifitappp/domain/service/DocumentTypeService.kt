package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.response.toDocumentTypeModels
import com.example.logifitappp.domain.repository.DocumentTypeRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

class DocumentTypeService @Inject constructor (private val documentTypeRepository: DocumentTypeRepository) {
    suspend fun all() = withContext (Dispatchers.IO) {
        try {
            val response = documentTypeRepository.all()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body()?.toDocumentTypeModels() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}
