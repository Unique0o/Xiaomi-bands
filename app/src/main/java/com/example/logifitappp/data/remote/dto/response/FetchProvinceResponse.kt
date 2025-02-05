package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.ProvinceModel
import com.google.gson.annotations.SerializedName

data class ProvinceResponse(
    @SerializedName("departament_id") val departmentId: Int,
    val id: Int,
    val name: String
)

data class FetchProvinceResponse(
    val data: List<ProvinceResponse>
)

fun FetchProvinceResponse.toProvinceModels() = data.map {
    ProvinceModel(
        departmentId = it.departmentId,
        id = it.id,
        name = it.name
    )
}