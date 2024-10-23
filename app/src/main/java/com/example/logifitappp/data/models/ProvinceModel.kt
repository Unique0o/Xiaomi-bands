package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provinces")
data class ProvinceModel(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "external_identifier") val externalIdentifier: Int,
    @ColumnInfo(name = "department_external_identifier") val departmentExternalIdentifier: Int,
    @ColumnInfo(name = "name") val name: String
)