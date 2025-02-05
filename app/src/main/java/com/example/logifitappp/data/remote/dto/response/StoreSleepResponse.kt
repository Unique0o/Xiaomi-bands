package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class StoreSleepResponse(
    @SerializedName("mac") val mac: String,
    @SerializedName("points") val points: Int
)
