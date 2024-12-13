package com.example.logifitappp.domain.service

import com.example.logifitappp.data.models.RestParameterModel
import com.example.logifitappp.data.remote.dto.response.GeneralErrorResponse
import com.example.logifitappp.data.remote.dto.response.TenantResponse
import com.example.logifitappp.data.remote.dto.response.toRestParameterModels
import com.example.logifitappp.domain.repository.TenantRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class TenantService @Inject constructor(private val tenantRepository: TenantRepository) {
    suspend fun fetch(): TenantResponse = withContext(Dispatchers.IO) {
        try {
            val response = tenantRepository.fetch()

            if (!response.isSuccessful) {
                response.errorBody()?.let {
                    val errorResponse = Gson().fromJson(it.string(), GeneralErrorResponse::class.java)
                    throw HttpConsumerException(AppStatusCodeEnum.fromCode(errorResponse.code))
                }

                throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
            }

            return@withContext response.body() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpConsumerException) {
            throw e
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchRestParameters(): List<RestParameterModel> = withContext(Dispatchers.IO) {
        try {
            val response = tenantRepository.fetchRestParameters()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body()?.toRestParameterModels() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}