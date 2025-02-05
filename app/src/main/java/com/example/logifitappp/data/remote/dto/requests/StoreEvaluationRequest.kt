package com.example.logifitappp.data.remote.dto.requests

data class EvaluationAnswerRequest(
    val answer: String,
    val detail: String? = null,
    val itemToolId: Int
)

data class StoreEvaluationRequest(
    val answers: List<EvaluationAnswerRequest>,
    val detail_tool_id: Int,
    val id: Int,
    val signature: String
)
