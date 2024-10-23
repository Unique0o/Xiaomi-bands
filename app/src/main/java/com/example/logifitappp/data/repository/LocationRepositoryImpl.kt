package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.LocationApi
import com.example.logifitappp.data.remote.dto.response.CountryResponse
import com.example.logifitappp.data.remote.dto.response.DepartmentResponse
import com.example.logifitappp.data.remote.dto.response.ProvinceResponse
import com.example.logifitappp.domain.repository.LocationRepository
import retrofit2.Response
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val api : LocationApi):
    LocationRepository {
     override suspend fun fetchCountries(): Response<List<CountryResponse>> {
        return api.fetchCountries()
    }

     override suspend fun fetchDepartments(): Response<List<DepartmentResponse>> {
        return api.fetchDepartments()
    }

     override suspend fun fetchProvinces(): Response<List<ProvinceResponse>> {
        return api.fetchProvinces()
    }
}

