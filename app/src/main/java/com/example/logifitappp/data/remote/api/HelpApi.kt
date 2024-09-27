package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.requests.StoreHelpRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface HelpApi {
    @POST("api/users/me/help")
    suspend fun store(@Body storeHelpRequest: StoreHelpRequest): Response<GeneralResponse>
}