package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class InformationResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
