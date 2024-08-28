package com.example.logifitappp.data.models

data class EvaluationModel(
    val id: Int,
    val description: String? = null,
    val externalIdentifier: Int,
    val indications: String? = null,
    val logoUrl: String? = null,
    val name: String,
    val notes: String? = null,
    val tenantExternalIdentifier: Int
)