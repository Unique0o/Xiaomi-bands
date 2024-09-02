package com.example.logifitappp.data.models

data class QuestionModel(
    val id: Int,
    val description: String,
    val evaluationExternalIdentifier: Int,
    val externalIdentifier: Int,
    val isRequired: Boolean,
    val options: String? = null,
    val type: String
)