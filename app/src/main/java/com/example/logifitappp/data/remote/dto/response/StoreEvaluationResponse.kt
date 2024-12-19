package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class StoreEvaluationResult(
    @SerializedName("condition") val condition: String,
    @SerializedName("date") val createdAt: String,
    @SerializedName("detail_tool_id") val evaluationId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("result") val result: String
)

data class StoreEvaluationResponse(
    val result: StoreEvaluationResult
)
