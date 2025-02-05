package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.response.toCountryModels
import com.example.logifitappp.data.remote.dto.response.toDepartmentModels
import com.example.logifitappp.data.remote.dto.response.toProvinceModels
import com.example.logifitappp.domain.repository.LocationRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class LocationService @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun fetchCountries() = withContext(Dispatchers.IO) {
        try {
            val response = locationRepository.fetchCountries()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body()?.toCountryModels() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchDepartments() = withContext(Dispatchers.IO) {
        try {
            val response = locationRepository.fetchDepartments()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body()?.toDepartmentModels() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchProvinces() = withContext(Dispatchers.IO) {
        try {
            val response = locationRepository.fetchProvinces()

            if (!response.isSuccessful) throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))

            return@withContext response.body()?.toProvinceModels() ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: HttpException) {
            throw HttpConsumerException(AppStatusCodeEnum.fromCode(e.code()))
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}

