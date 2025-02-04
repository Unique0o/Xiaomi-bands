package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.enums.AppStatusCodeEnum

data class WearableKeyUpdateState(
    val isLoading: Boolean = false,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.FETCHING_XIAOMI_CREDENTIALS
)
