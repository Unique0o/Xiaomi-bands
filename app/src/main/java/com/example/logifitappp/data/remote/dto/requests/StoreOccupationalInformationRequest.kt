package com.example.logifitappp.data.remote.dto.requests


data class StoreOccupationalInformationRequest(
    val attention: Int? = null,
    val break_after: Int? = null,
    val break_average: Int? = null,
    val break_frecuency: Int? = null,
    val commuting: Int? = null,
    val continuos_work: Int? = null,
    val location_aux_id: Int? = null,
    val shift_id: Int? = null,
    val workload: Int? = null
)