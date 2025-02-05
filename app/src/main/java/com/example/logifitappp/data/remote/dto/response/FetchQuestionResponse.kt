package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class QuestionTypeResponse(
    val options: String?,
    val type: String
)

data class QuestionItemResponse(
    val id: Int,
    @SerializedName("is_required") val isRequired: Int,
    val item: String,
    @SerializedName("type_item") val type: QuestionTypeResponse
)

data class FetchQuestionResponse(
    @SerializedName("test") val evaluation: FetchEvaluationResponse,
    @SerializedName("item") val items: List<QuestionItemResponse>
)
