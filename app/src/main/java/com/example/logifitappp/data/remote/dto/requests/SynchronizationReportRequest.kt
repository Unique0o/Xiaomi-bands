package com.example.logifitappp.data.remote.dto.requests

data class SynchronizationReportRequest(
    val date: String,
    val tenant_id: Int
)