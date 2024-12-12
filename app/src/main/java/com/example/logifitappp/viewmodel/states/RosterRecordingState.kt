package com.example.logifitappp.viewmodel.states

import android.icu.util.Calendar
import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.data.models.RosterLocationModel
import com.example.logifitappp.enums.AppStatusCodeEnum

data class RosterRecordingState(
    val comment: TextFieldValue = TextFieldValue(""),
    val endDate: Calendar? = null,
    val endDateError: String? = null,
    val hasRosterInformationStorageBeenSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val location: RosterLocationModel? = null,
    val locationError: String? = null,
    val startDate: Calendar? = null,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.STORING_ROSTER_INFORMATION
)