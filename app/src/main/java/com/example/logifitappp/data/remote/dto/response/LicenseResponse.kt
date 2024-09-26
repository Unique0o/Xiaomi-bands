package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class LicenseResponse(
    @SerializedName("type_license") val licenseType: String?,
)
