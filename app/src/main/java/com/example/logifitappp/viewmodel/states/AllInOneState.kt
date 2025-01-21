package com.example.logifitappp.viewmodel.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.ShiftModel

data class AllInOneState(
    var currentShift: ShiftModel? = null,
    var currentWearable: Wearable? = null,
    var searchText: TextFieldValue = TextFieldValue(""),
    var shouldShowUnpairWearableModal: Boolean = false,
    var shouldShowWearableShiftModal: Boolean = false
)
