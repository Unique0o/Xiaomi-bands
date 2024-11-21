package com.example.logifitappp.viewmodel.views.AddSleepData

import android.net.Uri
import com.example.logifitappp.data.models.SleepEntry
import java.time.LocalDateTime

data class AddSleepDataState(
    val sleepEntries: List<SleepEntry> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val photoUri: Uri? = null,
    val isValid: Boolean = false

)
