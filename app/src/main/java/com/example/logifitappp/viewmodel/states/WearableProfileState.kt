package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.enums.AppStatusCodeEnum

data class WearableProfileState(
    val activityDataSynchronizationMessage: String = "",
    val isLoading: Boolean = false,
    val isBandTheft: Boolean = false,
    val shift: ShiftModel? = null,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.FINDING_SMART_BAND,
    val synchronizationWithLogifitMessage: String = "",
    val totalSleepTimeMessage: String = "",
    val wearable: Wearable? = null
)
