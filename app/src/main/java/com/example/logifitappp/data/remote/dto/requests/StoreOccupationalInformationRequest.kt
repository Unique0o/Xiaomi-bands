package com.example.logifitappp.data.remote.dto.requests


data class StoreOccupationalInformationRequest(
    val attention: Int?,
    val break_after: Int?,
    val break_average: Int?,
    val break_frecuency: Int?,
    val commuting: Int?,
    val continuos_work: Int?,
    val location_aux_id: Int?,
    val shift_id: Int?,
    val workload: Int?
)