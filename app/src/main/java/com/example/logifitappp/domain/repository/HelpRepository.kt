package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.requests.StoreHelpRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import retrofit2.Response

interface HelpRepository {
    suspend fun store(storeHelpRequest: StoreHelpRequest): Response<GeneralResponse>
}