package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class FetchEvaluationResponse(
    val description: String?,
    val id: Int,
    @SerializedName("url_logo") val image: String?,
    val indications: String?,
    val name: String,
    val notes: String?
)
