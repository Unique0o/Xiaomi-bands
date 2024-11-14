package com.example.logifitappp.data.remote.dto.requests

data class StoreFatigueMetadataRequest(
    val identifier: String,
    val reem_cycles: Int? = null,
    val total_awake_time: String,
    val total_reem_sleep: String? = null,
    val total_sleep: String,
    val with_awakening_overcome: Int,
    val with_hypertension: Int,
    val with_little_reem_sleep: Int,
    val with_little_sleep: Int,
    val with_long_awake: Int
)

data class StoreFatigueRequest(
    val fatigue: StoreFatigueMetadataRequest
)
