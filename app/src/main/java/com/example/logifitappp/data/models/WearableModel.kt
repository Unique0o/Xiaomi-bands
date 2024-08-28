package com.example.logifitappp.data.models

data class WearableModel(
    val alias: String? = null,
    val firmwareVersion: String? = null,
    val id: Int,
    val mac: String,
    val name: String,
    val shiftExternalIdentifier: Int? = null,
    val userExternalIdentifier: Int
)