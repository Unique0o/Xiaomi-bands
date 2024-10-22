package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.CountryResponse
import com.example.logifitappp.data.remote.dto.response.DepartmentResponse
import com.example.logifitappp.data.remote.dto.response.ProvinceResponse
import retrofit2.Response
import retrofit2.http.GET

interface LocationApi {
    @GET("api/country/list")
    suspend fun fetchCountries(): Response<List<CountryResponse>>

    @GET("api/departaments")
    suspend fun fetchDepartments(): Response<List<DepartmentResponse>>

    @GET("api/provinces")
    suspend fun fetchProvinces(): Response<List<ProvinceResponse>>
}
