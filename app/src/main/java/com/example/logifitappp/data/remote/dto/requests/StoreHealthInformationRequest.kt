package com.example.logifitappp.data.remote.dto.requests


data class StoreHealthInformationRequest(
    val blood_type: String?,
    val gender: String?,
    val height: Float?,
    val weight: Float?
)