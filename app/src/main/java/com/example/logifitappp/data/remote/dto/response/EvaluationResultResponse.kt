package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.EvaluationResultModel
import com.google.gson.annotations.SerializedName

data class Evaluation(
    @SerializedName("name") val title: String
)

data class EvaluationResultResponse(
    @SerializedName("condition") val condition: String,
    @SerializedName("date") val createdAt: String,
    @SerializedName("test") val evaluation: Evaluation,
    @SerializedName("detail_tool_id") val evaluationId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("result") val result: String
)

fun EvaluationResultResponse.toEvaluationResultModel(userId: Int) = EvaluationResultModel(
    condition = condition,
    createdAt = createdAt,
    id = id,
    evaluationId = evaluationId,
    result = result,
    title = evaluation.title,
    userId = userId
)