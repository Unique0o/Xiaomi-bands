package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.ShiftModel
import com.google.gson.annotations.SerializedName

data class ShiftResponse(
    @SerializedName("days") val daysToApplySleepTimeExtension: String?,
    @SerializedName("end") val endAt: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("start") val startAt: String,
    @SerializedName("state") val state: String,
    @SerializedName("tenant_id") val tenantId: Int
)


fun ShiftResponse.toShiftModel() = ShiftModel(
    daysToApplySleepTimeExtension = daysToApplySleepTimeExtension,
    endTime = endAt,
    id = id,
    name = name,
    startTime = startAt,
    tenantId = tenantId
)