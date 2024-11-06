package com.example.logifitappp.data.remote.dto.requests

data class StoreHeartRateMetadataRequest(
    val level: String,
    val lpm: Int,
    val identifier: String
)

data class StoreHeartRateRequest(
    val heart_rate: List<StoreHeartRateMetadataRequest>
)
