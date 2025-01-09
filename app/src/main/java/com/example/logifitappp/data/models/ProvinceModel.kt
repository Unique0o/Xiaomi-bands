package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "provinces")
data class ProvinceModel(
    @ColumnInfo(name = "department_id") val departmentId: Int,
    @PrimaryKey val id: Int,
    val name: String
) {
    override fun toString() = name
}