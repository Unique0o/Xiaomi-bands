package com.example.logifitappp.data.models

data class CountryModel(
    val externalIdentifier: Int,
    val id: Int,
    val name: String
)

data class CountryPhoneCode(
    val name: String,
    val code: String,
    val flagResId: Int?,
    val id: String
)