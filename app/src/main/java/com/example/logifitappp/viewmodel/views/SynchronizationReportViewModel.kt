package com.example.logifitappp.viewmodel.views

import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.ColorUtils
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.SynchronizationReportRequest
import com.example.logifitappp.data.remote.dto.response.SynchronizationReportResponse
import com.example.logifitappp.domain.service.SynchronizationReportService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.ui.components.BottomSheetSelectableItem
import com.example.logifitappp.ui.theme.Stone240
import com.example.logifitappp.ui.theme.Stone510
import com.example.logifitappp.viewmodel.states.SynchronizationReportItemType
import com.example.logifitappp.viewmodel.states.SynchronizationReportState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SynchronizationReportViewModel.SynchronizationReportViewModelFactory::class)
class SynchronizationReportViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?,
    private val synchronizationService: SynchronizationReportService
): ViewModel() {
    @AssistedFactory
    interface SynchronizationReportViewModelFactory {
        fun create(user: UserModel?): SynchronizationReportViewModel
    }

    var state by mutableStateOf(SynchronizationReportState())
        private set

    init {
        fetchNecessaryData()
        fetchReport()
    }

    private fun applyFilters(
        report: Map<Int, List<SynchronizationReportItemType>>,
        specificKey: Int,
        group: BottomSheetSelectableItem,
        shift: BottomSheetSelectableItem
    ): Map<Int, List<SynchronizationReportItemType>> {
        val data = mutableMapOf<Int, List<SynchronizationReportItemType>>()

        report.keys.forEach { key ->
            data[key] = if (specificKey == key) report[key]!!.filter { (shift.id == 0 || shift.id == it.shiftId) &&(group.id == 0 || group.id == it.groupId) } else report[key]!!
        }

        return data
    }

    private fun checkIfCanGoToNext(calendar: Calendar): Boolean {
        return DateTimeUtils.format(calendar.time, "dd/MM/yyyy") != DateTimeUtils.format(Calendar.getInstance().time, "dd/MM/yyyy")
    }

    private fun fetchNecessaryData() {
        user?.tenantId?.let {
            state = state.copy(
                groups = listOf(object: BottomSheetSelectableItem(0) { override fun toString() = App.context.getString(R.string.all_of_them) }, *App.database.groupDao().all(it).toTypedArray()),
                shifts = listOf(object: BottomSheetSelectableItem(0) { override fun toString() = App.context.getString(R.string.all_of_them) }, *App.database.shiftDao().all(it).toTypedArray())
            )
        }
    }

    fun fetchReport() {
        if (user == null) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isFetchingReport = true,
                    status = AppStatusCodeEnum.FETCHING_SYNCHRONIZATION_REPORT
                )

                val report = synchronizationService.fetchReport(SynchronizationReportRequest(
                    date = DateTimeUtils.format(state.date.time, "yyyy-MM-dd"),
                    tenant_id = user.tenantId
                ))

                processReport(report).let {
                    state = state.copy(
                        currentTabIndex = 0,
                        conditions = report.conditions,
                        filteredReport = applyFilters(it, 0, state.selectedGroup, state.selectedShift),
                        hasFetchReportFailed = false,
                        isFetchingReport = false,
                        report = it
                    )
                }
            } catch (e: HttpConsumerException) {
                state = state.copy(isFetchingReport = false, hasFetchReportFailed = true)
            }
        }
    }

    fun handleChangeDateInMillis(timeInMillis: Long?) {
        val calendar = Calendar.getInstance()

        timeInMillis?.let { calendar.timeInMillis = it }

        state = state.copy(
            canGoToNextDay = checkIfCanGoToNext(calendar),
            date = calendar
        )

        fetchReport()
    }

    fun nextDay() {
        if (!state.canGoToNextDay) return

        val calendar = state.date.clone() as Calendar
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        state = state.copy(
            canGoToNextDay = checkIfCanGoToNext(calendar),
            date = calendar
        )

        fetchReport()
    }

    fun prevDay() {
        val calendar = state.date.clone() as Calendar
        calendar.add(Calendar.DAY_OF_MONTH, -1)

        state = state.copy(
            canGoToNextDay = true,
            date = calendar
        )

        fetchReport()
    }

    private fun processReport(report: SynchronizationReportResponse): MutableMap<Int, List<SynchronizationReportItemType>> {
        val data = mutableMapOf<Int, List<SynchronizationReportItemType>>()

        report.conditions.forEachIndexed { index, condition ->
            val items = mutableListOf<SynchronizationReportItemType>()

            if (condition.label == "S/D") {
                report.unsynchronizedUsers.forEach {
                    items.add(SynchronizationReportItemType(
                        background = Stone240,
                        color = Stone510,
                        condition = App.context.getString(R.string.no_data),
                        group = it.group?.name ?: App.context.getString(R.string.unassigned_shift_label),
                        groupId = it.group?.id,
                        label = it.fullName,
                        shift = it.shift?.name ?: App.context.getString(R.string.unassigned_shift_label),
                        shiftId = it.shift?.id
                    ))
                }
            } else {
                report.sleeps.forEach { sleep ->
                    items.add(SynchronizationReportItemType(
                        background = ColorUtils.toColor(sleep.backgroundColor ?: "#fadab1"),
                        color = ColorUtils.toColor(sleep.color ?: "#ffab40"),
                        condition = sleep.condition,
                        fatigue = sleep.fatigue?.let { fatigue ->
                            FatigueModel(
                                remCycles = fatigue.reemCycles,
                                totalAwakeSeconds = fatigue.totalAwakeTime.toLong(),
                                totalRemSeconds = fatigue.totalReemSleep?.toLong(),
                                totalSleepSeconds = fatigue.totalSleep.toLong(),
                                wearableId = 0,
                                withAwakeningOvercome = fatigue.withAwakeningOvercome == 1,
                                withHypertension = fatigue.withHypertension == 1,
                                withLittleReemSleep = fatigue.withLittleReemSleep == 1,
                                withLittleSleep = fatigue.withLittleSleep == 1,
                                withLongAwake = fatigue.withLongAwake == 1
                            )
                        },
                        group = sleep.group ?: App.context.getString(R.string.unassigned_shift_label),
                        groupId = sleep.groupId,
                        label = sleep.fullName,
                        shift = sleep.shift,
                        shiftId = sleep.shiftId
                    ))
                }
            }

            data[index] = items.toList()
        }

        return data
    }

    fun stopProcessing() {
        state = state.copy(
            isFetchingReport = false
        )
    }

    fun updateConditionIndex(index: Int) {
        state = state.copy(currentTabIndex = index, filteredReport = applyFilters(state.report, index, state.selectedGroup, state.selectedShift),)
    }

    fun updateGroup(group: BottomSheetSelectableItem) {
        state = state.copy(
            filteredReport = applyFilters(state.report, state.currentTabIndex, group, state.selectedShift),
            selectedGroup = group
        )
    }

    fun updateShift(shift: BottomSheetSelectableItem) {
        state = state.copy(
            filteredReport = applyFilters(state.report, state.currentTabIndex, state.selectedGroup, shift),
            selectedShift = shift
        )
    }
}