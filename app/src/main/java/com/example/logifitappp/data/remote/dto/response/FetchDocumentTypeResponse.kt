package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.DocumentTypeModel

data class DocumentTypeResponse(
    val id: Int,
    val name: String
)

data class FetchDocumentTypeResponse(
    val data: List<DocumentTypeResponse>
)

fun FetchDocumentTypeResponse.toDocumentTypeModels() = data.map {
    DocumentTypeModel(
        id = it.id,
        name = it.name
    )
}
