package com.example.logifitappp.data.remote.dto.requests

data class FetchXiaomiMacRequest(
    val credentials: Int,
    val password: String,
    val type: String?,
    val username: String
)
