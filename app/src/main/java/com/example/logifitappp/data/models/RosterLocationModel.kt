package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.ui.components.BottomSheetSelectableItem

@Entity(tableName = "roster_locations")
data class RosterLocationModel(
    @PrimaryKey override val id: Int,
    val name: String,
    @ColumnInfo(name = "tenant_id") val tenantId: Int
): BottomSheetSelectableItem(id) {
    override fun toString() = name
}
