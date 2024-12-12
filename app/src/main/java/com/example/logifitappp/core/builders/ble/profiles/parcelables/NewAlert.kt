package com.example.logifitappp.core.builders.ble.profiles.parcelables

import com.example.logifitappp.enums.AlertCategoryEnum

data class NewAlert(
    val categoryEnum: AlertCategoryEnum,
    val numAlerts: Int,
    val message: String?,
    val customIcon: Byte = -1
)