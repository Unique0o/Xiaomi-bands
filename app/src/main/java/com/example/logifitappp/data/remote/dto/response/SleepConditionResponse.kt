package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.SleepConditionModel
import com.google.gson.annotations.SerializedName

data class SleepConditionResponse(
    @SerializedName("background_color") val backgroundColor: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("condition") val condition: String,
    @SerializedName("end_parameter") val endParameter: Long,
    @SerializedName("id") val id: Int,
    @SerializedName("start_parameter") val startParameter: Long,
    @SerializedName("state") val state: Int,
    @SerializedName("tenant_id") val tenantId: Int
)

fun SleepConditionResponse.toSleepConditionModel() = SleepConditionModel(
    backgroundColor = backgroundColor ?: "#fadab1",
    color = color ?: "#ffab40",
    endSeconds = endParameter,
    id = id,
    name = condition,
    startSeconds = startParameter,
    tenantId = tenantId
)
