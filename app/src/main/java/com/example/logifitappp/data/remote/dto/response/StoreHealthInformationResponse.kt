package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class StoreHealthInformationResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("points")
    val points: Int
)
