package com.example.logifitappp.viewmodel.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.enums.AppStatusCodeEnum

data class WearableDetectionState(
    val authenticationKey: TextFieldValue = TextFieldValue(""),
    val currentCandidate: WearableCandidate? = null,
    val currentPage: Int = 0,
    val isBottomSheetVisible: Boolean = false,
    val isScanning: Boolean = false,
    val status: AppStatusCodeEnum? = null
)
