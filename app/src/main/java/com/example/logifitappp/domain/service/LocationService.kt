package com.example.logifitappp.domain.service

import com.example.logifitappp.data.remote.dto.response.CountryResponse
import com.example.logifitappp.data.remote.dto.response.DepartmentResponse
import com.example.logifitappp.data.remote.dto.response.ProvinceResponse
import com.example.logifitappp.domain.repository.LocationRepository
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

class LocationService @Inject constructor (private val locationRepository: LocationRepository) {

    suspend fun fetchCountries(): List<CountryResponse> = withContext (Dispatchers.IO)
    {
        try {
            val response = locationRepository.fetchCountries()
            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }
            return@withContext response.body()
                ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchDepartments(): List<DepartmentResponse> = withContext (Dispatchers.IO)
    {
        try {
            val response = locationRepository.fetchDepartments()
            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }
            return@withContext response.body()
                ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }

    suspend fun fetchProvinces(): List<ProvinceResponse> = withContext (Dispatchers.IO)
    {
        try {
            val response = locationRepository.fetchProvinces()
            if (!response.isSuccessful) {
                throw HttpConsumerException(AppStatusCodeEnum.fromCode(response.code()))
            }
            return@withContext response.body()
                ?: throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            throw HttpConsumerException(AppStatusCodeEnum.NO_INTERNET_CONNECTION)
        }
    }
}

