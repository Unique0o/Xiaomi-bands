package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.FetchDocumentTypeResponse
import retrofit2.Response

interface DocumentTypeRepository {
    suspend fun all(): Response<FetchDocumentTypeResponse>
}