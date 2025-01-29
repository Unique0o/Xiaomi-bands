package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import androidx.compose.ui.graphics.Color
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.remote.dto.response.ConditionResponse
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.ui.components.BottomSheetSelectableItem

data class SynchronizationReportState(
    var canGoToNextDay: Boolean = false,
    var conditions: List<ConditionResponse> = listOf(),
    var currentTabIndex: Int = 0,
    var date: Calendar = GregorianCalendar.getInstance(),
    var groups: List<BottomSheetSelectableItem> = listOf(),
    var filteredReport: Map<Int, List<SynchronizationReportItemType>> = mapOf(),
    var hasFetchReportFailed: Boolean = false,
    var isFetchingReport: Boolean = false,
    var report: Map<Int, List<SynchronizationReportItemType>> = mapOf(),
    var shifts: List<BottomSheetSelectableItem> = listOf(),
    var selectedGroup: BottomSheetSelectableItem = object: BottomSheetSelectableItem(0) { override fun toString() = App.context.getString(R.string.all_of_them) },
    var selectedShift: BottomSheetSelectableItem = object: BottomSheetSelectableItem(0) { override fun toString() = App.context.getString(R.string.all_of_them) },
    var status: AppStatusCodeEnum = AppStatusCodeEnum.FETCHING_SYNCHRONIZATION_REPORT,
    var total: Int = 0
)

data class SynchronizationReportItemType(
    val background: Color,
    val color: Color,
    val condition: String,
    val fatigue: FatigueModel? = null,
    val group: String,
    val groupId: Int? = null,
    val label: String,
    val shift: String,
    val shiftId: Int? = null
)
