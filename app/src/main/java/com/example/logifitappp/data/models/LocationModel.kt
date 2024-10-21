package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationModel(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(name = "tenant_id") val tenantId: Int
)