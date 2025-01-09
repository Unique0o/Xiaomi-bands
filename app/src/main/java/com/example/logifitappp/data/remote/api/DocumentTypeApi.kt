package com.example.logifitappp.data.remote.api

import com.example.logifitappp.data.remote.dto.response.FetchDocumentTypeResponse
import retrofit2.Response
import retrofit2.http.GET

interface DocumentTypeApi {
    @GET("api/document_type/list")
    suspend fun all(): Response<FetchDocumentTypeResponse>
}