package com.example.logifitappp.domain.repository


import com.example.logifitappp.data.remote.dto.response.CountryResponse
import com.example.logifitappp.data.remote.dto.response.DepartmentResponse
import com.example.logifitappp.data.remote.dto.response.ProvinceResponse
import retrofit2.Response

interface LocationRepository {
    suspend fun fetchCountries(): Response<List<CountryResponse>>
    suspend fun fetchDepartments(): Response<List<DepartmentResponse>>
    suspend fun fetchProvinces(): Response<List<ProvinceResponse>>
}
