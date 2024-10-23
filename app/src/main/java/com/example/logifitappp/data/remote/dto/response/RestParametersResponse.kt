package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.RestParameterModel
import com.google.gson.annotations.SerializedName

data class RestParameterInfo(
    @SerializedName("type") val type: String
)

data class RestParameter(
    @SerializedName("error_label") val errorLabel: String,
    @SerializedName("parameter") val info: RestParameterInfo,
    @SerializedName("measurement") val measurement: String,
    @SerializedName("success_label") val successLabel: String,
    @SerializedName("tenant_id") val tenantId: Int
)

data class RestParametersResponse(
    @SerializedName("data") val data: List<RestParameter>
)

fun RestParametersResponse.toRestParameterModels() = data.map {
    RestParameterModel(
        errorLabel = it.errorLabel,
        measurement = it.measurement,
        successLabel = it.successLabel,
        tenantId = it.tenantId,
        type = it.info.type
    )
}