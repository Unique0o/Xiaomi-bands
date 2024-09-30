package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class WorkerResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)