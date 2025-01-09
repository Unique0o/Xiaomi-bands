package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.LocationApi
import com.example.logifitappp.data.remote.dto.response.FetchCountryResponse
import com.example.logifitappp.data.remote.dto.response.FetchDepartmentResponse
import com.example.logifitappp.data.remote.dto.response.FetchProvinceResponse
import com.example.logifitappp.domain.repository.LocationRepository
import retrofit2.Response
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val locationApi: LocationApi): LocationRepository {
     override suspend fun fetchCountries(): Response<FetchCountryResponse> {
        return locationApi.fetchCountries()
    }

     override suspend fun fetchDepartments(): Response<FetchDepartmentResponse> {
        return locationApi.fetchDepartments()
    }

     override suspend fun fetchProvinces(): Response<FetchProvinceResponse> {
        return locationApi.fetchProvinces()
    }
}

