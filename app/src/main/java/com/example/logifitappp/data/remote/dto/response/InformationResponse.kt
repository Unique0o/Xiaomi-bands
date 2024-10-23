package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class InformationResponse(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int
)
