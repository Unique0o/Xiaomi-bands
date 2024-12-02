package com.example.logifitappp.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class FetchRosterDetailResponse(
    @SerializedName("current_page") val currentPage: Int,
    val data: List<RosterDetailResponse>,
    @SerializedName("last_page") val lastPage: Int,
)

data class RosterDetailResponse(
    @SerializedName("comentario") val comment: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("fecha_salida") val endDate: String,
    val location: RosterLocationDetailResponse,
    @SerializedName("fecha_retorno") val startDate: String,
    @SerializedName("estado") val status: String
)

data class RosterLocationDetailResponse(
    val id: Int,
    val name: String
)
