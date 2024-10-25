package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.enums.AppStatusCodeEnum

data class HomeState(
    val isLoading: Boolean = false,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.CONNECTING_WITH_WEARABLE
)
