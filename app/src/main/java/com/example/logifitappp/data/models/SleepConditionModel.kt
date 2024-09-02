package com.example.logifitappp.data.models

data class SleepConditionModel(
    val id: Int,
    val backgroundColor: String,
    val color: String,
    val endSeconds: Int,
    val externalIdentifier: Int,
    val name: String,
    val startSeconds: Int,
    val tenantExternalIdentifier: Int
)
