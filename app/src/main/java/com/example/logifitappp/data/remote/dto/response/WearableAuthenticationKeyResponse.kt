package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class WearableAuthenticationKeyResponse(
    @SerializedName("data") val key: String?
)
