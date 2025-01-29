package com.example.logifitappp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.logifitappp.ui.components.BottomSheetSelectableItem


@Entity(tableName = "provinces")
data class ProvinceModel(
    @ColumnInfo(name = "department_id") val departmentId: Int,
    @PrimaryKey override val id: Int,
    val name: String
):BottomSheetSelectableItem (id){
    override fun toString() = name
}