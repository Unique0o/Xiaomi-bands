package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.DocumentTypeApi
import com.example.logifitappp.data.remote.dto.response.FetchDocumentTypeResponse
import com.example.logifitappp.domain.repository.DocumentTypeRepository
import retrofit2.Response
import javax.inject.Inject

class DocumentTypeRepositoryImpl @Inject constructor(private val documentTypeApi : DocumentTypeApi): DocumentTypeRepository {
    override suspend fun all(): Response<FetchDocumentTypeResponse> {
        return documentTypeApi.all()
    }
}
