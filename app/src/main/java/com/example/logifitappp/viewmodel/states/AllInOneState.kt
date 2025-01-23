package com.example.logifitappp.viewmodel.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.AppStatusCodeEnum

data class AllInOneState(
    var currentShift: ShiftModel? = null,
    var currentWearable: Wearable? = null,
    var fetchingWorkersStatus: AppStatusCodeEnum = AppStatusCodeEnum.FETCHING_WORKER_LIST,
    var isFetchingWorkers: Boolean = false,
    var searchText: TextFieldValue = TextFieldValue(""),
    var shouldShowUnpairWearableModal: Boolean = false,
    var shouldShowWearableShiftModal: Boolean = false
)
