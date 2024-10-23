package com.example.logifitappp.data.models

import androidx.room.PrimaryKey

data class DepartmentModel(
    @PrimaryKey val countryExternalIdentifier: Int,
    val id: Int? = null,
    val externalIdentifier: Int,
    val name: String
)