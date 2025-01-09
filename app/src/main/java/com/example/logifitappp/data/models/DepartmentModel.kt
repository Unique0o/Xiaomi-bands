package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class DepartmentModel(
    @ColumnInfo(name = "country_id") val countryId: Int,
    @PrimaryKey val id: Int,
   val name: String
) {
    override fun toString() = name
}