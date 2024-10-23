package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.DocumentTypeApi
import com.example.logifitappp.data.remote.dto.response.DocumentTypeResponse
import com.example.logifitappp.domain.repository.DocumentTypeRepository
import retrofit2.Response
import javax.inject.Inject

class DocumentTypeRepositoryImpl @Inject constructor(private val api : DocumentTypeApi):
    DocumentTypeRepository {
    override suspend fun all(): Response<DocumentTypeResponse> {
        return api.all()
    }
}
