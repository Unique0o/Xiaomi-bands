package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.FetchCountryResponse
import com.example.logifitappp.data.remote.dto.response.FetchDepartmentResponse
import com.example.logifitappp.data.remote.dto.response.FetchProvinceResponse
import retrofit2.Response
import retrofit2.http.GET

interface LocationApi {
    @GET("api/country/list")
    suspend fun fetchCountries(): Response<FetchCountryResponse>

    @GET("api/departaments")
    suspend fun fetchDepartments(): Response<FetchDepartmentResponse>

    @GET("api/provinces")
    suspend fun fetchProvinces(): Response<FetchProvinceResponse>
}
