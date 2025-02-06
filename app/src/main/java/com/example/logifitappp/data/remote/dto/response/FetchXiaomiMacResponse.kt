package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class FetchXiaomiMacResponse(
    @SerializedName("ID") val id: Int,
    @SerializedName("MAC") val mac: String,
    @SerializedName("TOKEN") val token: String,
    val type: String?
)
