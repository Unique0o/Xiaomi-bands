package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.TermsAndConditionsResponse
import retrofit2.http.GET
import retrofit2.Response

interface TermsAndConditionsApi {
    @GET("api/terminos_condiciones")
    suspend fun getTermsAndConditions(): Response<TermsAndConditionsResponse>
}