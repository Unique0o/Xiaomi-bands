package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.ui.components.BottomSheetSelectableItem


@Entity(tableName = "departments")
data class DepartmentModel(
    @ColumnInfo(name = "country_id") val countryId: Int,
    @PrimaryKey override val id: Int,
   val name: String
): BottomSheetSelectableItem(id){
    override fun toString() = name
}