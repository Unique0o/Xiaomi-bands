package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.DepartmentModel
import com.google.gson.annotations.SerializedName

data class DepartmentResponse(
    @SerializedName("country_id") val countryId: Int,
    val id: Int,
    val name: String
)

data class FetchDepartmentResponse(
    val data: List<DepartmentResponse>
)

fun FetchDepartmentResponse.toDepartmentModels() = data.map {
    DepartmentModel(
        countryId = it.countryId,
        id = it.id,
        name = it.name
    )
}