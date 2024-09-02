package com.example.logifitappp.data.models

data class RestParameterModel(
    val id: Int,
    val errorLabel: String,
    val measurement: String,
    val successLabel: String,
    val tenantExternalIdentifier: Int,
    val type: String
)