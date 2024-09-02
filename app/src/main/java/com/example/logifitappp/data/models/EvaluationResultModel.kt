package com.example.logifitappp.data.models

data class EvaluationResultModel(
    val id: Int,
    val condition: String,
    val createdAt: String,
    val evaluationExternalIdentifier: Int,
    val externalIdentifier: Int,
    val result: String,
    val title: String,
    val userExternalIdentifier: Int
)