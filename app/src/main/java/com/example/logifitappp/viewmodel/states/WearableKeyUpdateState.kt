package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.data.remote.dto.response.FetchXiaomiCredentialSelectableItem
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.ui.components.BottomSheetSelectableItem

data class WearableKeyUpdateState(
    val credential: FetchXiaomiCredentialSelectableItem? = null,
    val hasNecessaryDataFetchingFailed: Boolean = false,
    val isNecessaryDataFetching: Boolean = true,
    val isLoading: Boolean = false,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.FETCHING_XIAOMI_MACS,
    val type: BottomSheetSelectableItem? = null
)
