package com.example.logifitappp.ui.screens.synchronization_report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.forms.DateField
import com.example.logifitappp.ui.components.forms.SelectableBottomSheet
import com.example.logifitappp.viewmodel.views.SynchronizationReportViewModel

@Composable
fun SynchronizationReportFilters(
    synchronizationReportViewModel: SynchronizationReportViewModel
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        DateField(
            canGoToNextDay = synchronizationReportViewModel.state.canGoToNextDay,
            date = synchronizationReportViewModel.state.date.time,
            onDateSelected = { synchronizationReportViewModel.handleChangeDateInMillis(it) },
            onNextDay = { synchronizationReportViewModel.nextDay() },
            onPrevDay = { synchronizationReportViewModel.prevDay() }
        )

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectableBottomSheet(
                elements = synchronizationReportViewModel.state.shifts,
                modifier = Modifier.weight(1f),
                onChange = { synchronizationReportViewModel.updateShift(it) },
                placeholder = stringResource(R.string.placeholder_shift),
                title = stringResource(R.string.shift_title),
                value = synchronizationReportViewModel.state.selectedShift
            )

            Spacer(Modifier.width(4.dp))

            SelectableBottomSheet(
                elements = synchronizationReportViewModel.state.groups,
                modifier = Modifier.weight(1f),
                onChange = { synchronizationReportViewModel.updateGroup(it) },
                placeholder = stringResource(R.string.placeholder_group),
                title = stringResource(R.string.group_title),
                value = synchronizationReportViewModel.state.selectedGroup
            )
        }
    }
}