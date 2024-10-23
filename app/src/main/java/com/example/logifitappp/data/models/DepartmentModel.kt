package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentModel(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "external_identifier") val externalIdentifier: Int,
    @ColumnInfo(name = "country_external_identifier") val countryExternalIdentifier: Int,
    @ColumnInfo(name = "name") val name: String
)