package com.example.logifitappp.data.models


data class SynchronizationReportItemModel(
    val backgroundColor: String,
    val color: String,
    val condition: String,
    val fatigue: FatigueModel? = null,
    val group: String? = null,
    val label: String,
    val shift: String
)