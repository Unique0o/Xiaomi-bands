package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class WorkerItemResponse(
    @SerializedName("dni") val document: String?,
    @SerializedName("first_name") val firstName: String,
    val id: Int,
    @SerializedName("last_name") val lastName: String
)

data class WorkerResponse(
    val data: List<WorkerItemResponse>
)