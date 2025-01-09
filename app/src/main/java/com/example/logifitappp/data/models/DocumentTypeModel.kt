package com.example.logifitappp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.ui.components.BottomSheetSelectableItem

@Entity(tableName = "document_types")
data class DocumentTypeModel(
    @PrimaryKey override val id: Int,
    val name: String
): BottomSheetSelectableItem(id) {
    override fun toString() = name
}