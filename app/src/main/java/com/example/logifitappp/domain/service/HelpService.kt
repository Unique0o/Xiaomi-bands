package com.example.logifitappp.domain.service


import com.example.logifitappp.data.remote.dto.requests.StoreHelpRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.domain.repository.HelpRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HelpService  @Inject constructor (private val repository: HelpRepository) {
    suspend fun store(storeHelpRequest: StoreHelpRequest): GeneralResponse = withContext(
        Dispatchers.IO) {
        try {
            val response = repository.store(storeHelpRequest)
            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }
            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}