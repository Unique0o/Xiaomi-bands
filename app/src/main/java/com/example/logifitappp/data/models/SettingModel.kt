package com.example.logifitappp.data.models

data class SettingModel(
    val id: Int,
    val key: Int,
    val modifiableIdentifier: Int,
    //val modifiableType: AppSettingModelEnum,
    val value: String? = null
)