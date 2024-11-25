package com.example.logifitappp.data.remote.dto.requests

data class StoreRosterRequest(
    val fecha_retorno: String,
    val fecha_salida: String,
    val location_id: Int,
    val user_id: Int
)
