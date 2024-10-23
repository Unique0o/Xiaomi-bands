package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rest_parameters")
data class RestParameterModel(
    @ColumnInfo(name = "error_label") val errorLabel: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val measurement: String,
    @ColumnInfo(name = "success_label") val successLabel: String,
    @ColumnInfo(name = "tenant_id") val tenantId: Int,
    val type: String
)