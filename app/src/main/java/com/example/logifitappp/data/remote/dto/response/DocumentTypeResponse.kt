package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class DocumentType(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class DocumentTypeResponse(
    @SerializedName("documents") val documents: List<DocumentType>
)

