package com.example.logifitappp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.ui.components.BottomSheetSelectableItem

@Entity(tableName = "countries")
data class CountryModel(
    @PrimaryKey override val id: Int,
    val name: String
): BottomSheetSelectableItem(id) {
    override fun toString() = name
}