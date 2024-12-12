package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.data.models.DrowsinessModel
import com.example.logifitappp.data.models.FatigueModel
import com.example.logifitappp.data.models.LocationModel
import com.example.logifitappp.data.models.ShiftModel
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.enums.AppStatusCodeEnum

data class HomeState(
    val drowsiness: DrowsinessModel? = null,
    val drowsinessCondition: SleepConditionModel? = null,
    val fatigue: FatigueModel? = null,
    val isBandTheft: Boolean = false,
    val isLoading: Boolean = false,
    val isSleepSynchronizationRequired: Boolean = true,
    val isSynchronizationWithLogifitRequired: Boolean = true,
    val location: LocationModel? = null,
    val shift: ShiftModel? = null,
    val tenant: TenantModel? = null,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.CONNECTING_WITH_WEARABLE
)
