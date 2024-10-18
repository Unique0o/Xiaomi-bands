package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.remote.dto.response.DocumentTypeResponse
import retrofit2.Response

interface DocumentTypeRepository {
    suspend fun all(): Response<DocumentTypeResponse>
}