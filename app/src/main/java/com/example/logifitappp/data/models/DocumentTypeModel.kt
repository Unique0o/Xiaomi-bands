package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document_types")
data class DocumentTypeModel(
    @ColumnInfo(name = "externalIdentifier") val externalIdentifier: Int,
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "name") val name: String = ""
)