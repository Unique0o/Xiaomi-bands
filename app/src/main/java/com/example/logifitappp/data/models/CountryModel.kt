package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryModel(
    @ColumnInfo(name = "externalIdentifier") val externalIdentifier: Int,
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "name") val name: String = ""
)

data class CountryPhoneCode(
    val name: String,
    val code: String,
    val flagResId: Int?,
    val id: String
)