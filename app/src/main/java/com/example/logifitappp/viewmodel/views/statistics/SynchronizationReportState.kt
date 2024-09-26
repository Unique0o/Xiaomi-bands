package com.example.logifitappp.viewmodel.views.statistics

import com.example.logifitappp.data.models.SynchronizationReportItemModel
import java.time.LocalDate
import com.example.logifitappp.di.services.responses.Condition

data class SynchronizationReportState(
    val condition: Condition,
    val data: List<SynchronizationReportItemModel>,
    val date: LocalDate,
    val group: String,
    val shift: String,
    val total: Int,
)
