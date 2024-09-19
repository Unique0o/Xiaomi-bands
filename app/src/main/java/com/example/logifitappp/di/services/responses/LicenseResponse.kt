package com.example.logifitappp.di.services.responses

import com.google.gson.annotations.SerializedName

data class LicenseResponse(
    @SerializedName("type_license") val licenseType: String?,
)
