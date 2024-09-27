package com.example.logifitappp.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class StorePersonalInformationResponse(
    val message: String,
    val points: Int
)