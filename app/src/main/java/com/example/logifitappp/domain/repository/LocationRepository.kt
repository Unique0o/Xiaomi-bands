package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.FetchCountryResponse
import com.example.logifitappp.data.remote.dto.response.FetchDepartmentResponse
import com.example.logifitappp.data.remote.dto.response.FetchProvinceResponse
import retrofit2.Response

interface LocationRepository {
    suspend fun fetchCountries(): Response<FetchCountryResponse>
    suspend fun fetchDepartments(): Response<FetchDepartmentResponse>
    suspend fun fetchProvinces(): Response<FetchProvinceResponse>
}
